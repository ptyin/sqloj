from flask_restx import Namespace, Resource, fields
from flask_login import login_required, current_user

from bson.objectid import ObjectId

from datetime import datetime

from .extension import mongo, login_manager

api = Namespace('student', description="Students' manipulations")

assignment_detail = api.model('Assignment detail', {
    'assignment_id': fields.String(required=True, description="Assignment unique id"),
    'assignment_name': fields.String(required=True, description="Assignment name"),
    'assignment_start_time	': fields.String(required=True, description="Assignment start time"),
    'assignment_end_time': fields.String(required=True, description="Assignment deadline")
})

question_status = api.model('Question status', {
    'question_id': fields.String(required=True, description="Question unique id"),
    'question_name': fields.String(required=True, description="Question name for displaying"),
    'is_finished	': fields.Boolean(required=True, description="Question finish status"),
})

question_detail = api.model('Question detail', {
    'question_name': fields.String(required=True, description="Question name"),
    'question_description': fields.String(required=True, description="Question description"),
    'question_output	': fields.String(required=True, description="Required question output"),
})

submit_status = api.model('Answer submit status', {
    'success': fields.Boolean(required=True, description="whether the code uploads successfully"),
})


@login_required
@api.route("/queryAssignmentList")
@api.doc(description="Get assignment list and their time span")
class AssignmentListQuery(Resource):
    @api.marshal_with(assignment_detail, as_list=True)
    def get(self):
        assignments = list(mongo.db.assignments.find({}))
        print(assignments, "in")
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
    @api.marshal_with(question_status, as_list=True)
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
@api.route("/selectQuestionsById")
@api.doc(description="Get question detail of the requested question id")
class QuestionListQuery(Resource):
    @api.doc(parser=quesId_parser)
    @api.marshal_with(question_detail)
    def get(self):
        args = quesId_parser.parse_args()
        print("question_id " + str(args["question_id"]))
        q = mongo.db.questions.find_one({"question_id": args["question_id"]})
        print(q)
        return {
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
class QuestionListQuery(Resource):
    @api.doc(parser=answer_parser)
    @api.marshal_with(submit_status)
    def post(self):
        args = answer_parser.parse_args()
        # print(args["question_id"])
        q = mongo.db.questions.find_one({"question_id": args["question_id"]})
        if q is None:
            return {"success": False}

        new_record = {
            "record_id": "r-" + str(ObjectId()),
            "question_id": str(args["question_id"]),
            "assignment_id": str(q["assignment_id"]),
            "username": str(current_user.id),
            "submit_time": datetime.now(),
            "record_code": str(args["code"]),
            "record_status": "RUNNING",
            "running_time": None
        }
        mongo.db.records.insert_one(new_record)
        # todo call judge function
        return {"success": True}


# todo
@login_manager.unauthorized_handler
def unauthorized_handler():
    print("user not login")
    return 'login_required'
