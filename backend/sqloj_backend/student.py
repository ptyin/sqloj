from flask_restx import Namespace, Resource
from flask_login import login_required, current_user

from bson.objectid import ObjectId

from threading import Thread
from datetime import datetime

from .extension import mongo, login_manager
from .model import *
from .util import insert_one_document, update_one_document
from .task import get_user_answer_output

api = Namespace('student', description="Students' manipulations")

assignment_list_res = api.model('Assignment list', assignment_detail)

assignment_detail_req = api.model("Assignment's question request", assignment_detail_req_model)

question_status_res = api.model('Question status', question_status)

question_detail_req = api.model("Question detail request", question_detail_req_model)

question_detail_res = api.model('Question detail', question_detail)

submit_status_res = api.model('Answer submit status', {
    'success': fields.Boolean(required=True, description="whether the code uploads successfully"),
})

records_list_res = api.model('Records list with details', records_list)

record_detail_res = api.model('Record detail', record_output_res_model)


@login_required
@api.route("/queryAssignmentList")
@api.doc(description="Get assignment list and their time span")
class AssignmentListQuery(Resource):
    @api.marshal_with(assignment_list_res, as_list=True)
    def get(self):
        assignments = list(mongo.db.assignments.find({}))
        # print(assignments, "in")
        return [{
            "assignment_id": str(i["assignment_id"]),
            "assignment_name": str(i["assignment_name"]),
            "assignment_start_time": i["assignment_start_time"].strftime('%B %d, %Y %H:%M:%S'),
            "assignment_end_time": i["assignment_end_time"].strftime('%B %d, %Y %H:%M:%S'),
        } for i in assignments]


assignId_parser = api.parser()
assignId_parser.add_argument(
    "assignment_id", type=str, required=True, help="Requested assignment id"
)


@login_required
@api.route("/selectQuestionsByAssignment")
@api.doc(description="Get question list of the requested assignment")
class QuestionListQuery(Resource):
    @api.doc(parser=assignId_parser)
    @api.expect(assignment_detail_req)
    @api.marshal_with(question_status_res, as_list=True)
    def get(self):
        args = assignId_parser.parse_args()
        # print(args["assignment_id"])
        questions = list(mongo.db.questions.find({"assignment_id": args["assignment_id"]}))
        # print(questions)
        # print(current_user.id)
        # print(mongo.db.records.find_one(
        #        {"assignment_id": "a-066ab87a062b",
        #         "question_id": "q-21ru2933hui4",
        #         "username": str(current_user.id),
        #         "record_status": "AC"
        #         }))
        return [{
            "question_id": str(i["question_id"]),
            "question_name": str(i["question_name"]),
            "is_finished": False if mongo.db.records.find_one(
                {"assignment_id": args["assignment_id"],
                 "question_id": str(i["question_id"]),
                 "username": str(current_user.id),
                 "record_status": "AC"
                 }) is None else True
        } for i in questions]


quesId_parser = api.parser()
quesId_parser.add_argument(
    "question_id", type=str, required=True, help="Requested question id"
)


@login_required
@api.route("/selectQuestionById")
@api.doc(description="Get question detail of the requested question id")
class QuestionDetailQuery(Resource):
    @api.doc(parser=quesId_parser)
    @api.expect(question_detail_req)
    @api.marshal_with(question_detail_res)
    def get(self):
        args = quesId_parser.parse_args()
        # print("question_id " + str(args["question_id"]))
        q = mongo.db.questions.find_one({"question_id": args["question_id"]})
        # print(q)
        return {
            "question_type": str(q["question_type"]),
            "question_name": str(q["question_name"]),
            "question_description": str(q["question_description"]),
            "question_output": str(q["question_output"])
        }


answer_parser = api.parser()
answer_parser.add_argument(
    "question_id", type=str, required=True, help="Answered question id",
)
answer_parser.add_argument(
    "code", type=str, required=True, help="Sql code to execute",
)


@login_required
@api.route("/submit")
@api.doc(description="Upload submitted code of the question to the judger")
class QuestionSubmit(Resource):
    @api.doc(parser=answer_parser)
    @api.marshal_with(submit_status_res)
    def post(self):
        args = answer_parser.parse_args()
        # print(args["question_id"])
        q = mongo.db.questions.find_one({"question_id": args["question_id"]})
        if q is None:
            return {"success": False}
        record_id = "r-" + str(ObjectId())
        submit_time = datetime.now()
        new_record = {
            "record_id": record_id,
            "question_id": str(args["question_id"]),
            "question_type": str(q["question_type"]),
            "assignment_id": str(q["assignment_id"]),
            "username": str(current_user.id),
            "submit_time": submit_time,
            "record_code": str(args["code"]),
            "record_status": "RUNNING" if str(q["question_type"]) == "sql" else None,
            "running_time": 0
        }
        insert_status = insert_one_document(mongo.db.records, new_record)
        if str(q["question_type"]) == "sql":
            # call judger function, update record
            Thread(target=get_user_answer_output,
                   args=(
                   record_id, str(args["code"]), str(args["question_id"]), submit_time, str(current_user.id))).start()
        return {"success": insert_status}


@login_required
@api.route("/queryRecordList")
@api.doc(description="Get record list")
class RecordListQuery(Resource):
    @api.marshal_with(records_list_res, as_list=True)
    def get(self):
        records = list(mongo.db.records.find({"username": current_user.id}))
        # print(records)
        return [{
            'record_id': str(r["record_id"]),
            'record_time': r["submit_time"].strftime('%B %d, %Y %H:%M:%S'),
            'assignment_id': str(r["assignment_id"]),
            'assignment_name': str(mongo.db.assignments.find_one(
                {"assignment_id": str(r["assignment_id"])})["assignment_name"]),
            'question_id': str(r["question_id"]),
            'question_type': str(r["question_type"]),
            'question_name': str(mongo.db.questions.find_one({"question_id": str(r["question_id"])})[
                                     "question_name"]),
            'record_status': str(r["record_status"]),
            'running_time': str(r["running_time"]) if r["question_type"] == "sql" and r[
                "record_status"] != "RUNNING" else None
        } for r in records]


recId_parser = api.parser()
recId_parser.add_argument(
    "record_id", type=str, required=True, help="Requested record id"
)


@login_required
@api.route("/selectRecordById")
@api.doc(description="Get submitted code for a specific record")
class RecordDetailQuery(Resource):
    @api.doc(parser=recId_parser)
    @api.marshal_with(record_detail_res)
    def get(self):
        args = quesId_parser.parse_args()
        # print("question_id " + str(args["question_id"]))
        record = mongo.db.records.find_one({"record_id": args["record_id"]})
        output = mongo.db.record_outputs.find_one({"record_id": args["record_id"]})
        return {
            "username": str(record["username"]),
            "submit_time": record["submit_time"].strftime('%B %d, %Y %H:%M:%S'),
            "finished_time": record["finished_time"].strftime('%B %d, %Y %H:%M:%S')
            if record["question_type"] == "sql" and record["record_status"] != "RUNNING" else None,
            'question_type': str(record["question_type"]),
            "record_code": str(record["record_code"]),
            "record_status": str(record["record_status"]) if record["question_type"] == "sql" else None,
            "output": str(output["output"]) if record["question_type"] == "sql" and output else None,
            "record_lack": str(record["record_lack"]) if record["question_type"] == "sql" else None,
            "record_err": str(record["record_err"]) if record["question_type"] == "sql" else None,
        }


# todo
@login_manager.unauthorized_handler
def unauthorized_handler():
    print("user not login")
    return 'login_required'
