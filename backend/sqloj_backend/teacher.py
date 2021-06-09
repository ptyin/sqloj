from flask_restx import Namespace, Resource
from flask_login import login_required, current_user

from bson.objectid import ObjectId
from werkzeug.datastructures import FileStorage

from datetime import datetime

from .model import *
from .extension import mongo, login_manager
from .util import insert_one_document, update_one_document, delete_many_document

api = Namespace('teacher', description="Students' manipulations")

assignment_list_res = api.model('Assignment detail', assignment_detail)

add_assignment_req = api.model('Added assignment detail', assignment_adding_detail)

modify_assignment_req = api.model('Modified assignment detail', assignment_detail)

delete_assignment_req = api.model("Delete assignment request", assignment_detail_req_model)

modify_assignment_res = api.model('Assignment adding status', add_assignment_res_model)

question_list_res = api.model('Model', question_list)

add_question_req = api.model('Added question detail',
                             {**question_detail,
                              **{'assignment_id':
                                     fields.String(required=True,
                                                   description="Assignment id where the question lies"),
                                 'db_id': fields.String(required=True,
                                                        description="Database id the question uses"),
                                 }})

get_question_res = api.model('Question detail', question_detail_all_model)

modify_question_req = api.model('Modified question detail', question_detail_all_model)
delete_question_req = api.model("Delete question request", question_detail_req_model)
modify_question_res = api.model('Question adding status', add_question_res_model)

db_list_res = api.model('DB list', db_list_res_model)

db_detail_full_res=api.model('DB detail', db_detail_full_model)

add_db_req = api.model('Added DB detail', db_add_req_model)

modify_db_req = api.model('Modified DB detail', db_modify_req_model)

delete_db_req = api.model("Delete assignment request", db_delete_req_model)

modify_db_res = api.model('Assignment adding status', add_db_res_model)

# todo judge user role

upload_parser = api.parser()
upload_parser.add_argument('file', location='files',
                           type=FileStorage, required=True)


@login_required
@api.route("/AssignmentListQuery")
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


post_assignment_parser = api.parser()
post_assignment_parser.add_argument(
    "assignment_name", type=str, required=True,
)
post_assignment_parser.add_argument(
    "assignment_start_time", type=str, required=True,
)

post_assignment_parser.add_argument(
    "assignment_end_time", type=str, required=True,
)

modify_assignment_parser = api.parser()
modify_assignment_parser.add_argument(
    "assignment_id", type=str, required=True,
)
modify_assignment_parser.add_argument(
    "assignment_name", type=str, required=True,
)
modify_assignment_parser.add_argument(
    "assignment_start_time", type=str, required=True,
)

modify_assignment_parser.add_argument(
    "assignment_end_time", type=str, required=True,
)

delete_assignment_parser = api.parser()
delete_assignment_parser.add_argument(
    "assignment_id", type=str, required=True,
)


@login_required
@api.route("/AssignmentDetail")
class AssignmentDetail(Resource):
    @api.doc(parser=post_assignment_parser)
    @api.expect(add_assignment_req)
    @api.marshal_with(modify_assignment_res)
    def post(self):
        args = post_assignment_parser.parse_args()
        assignment_id = "a-" + str(ObjectId())
        new_assignment = {
            "assignment_id": assignment_id,
            "assignment_name": str(args["assignment_name"]),
            "assignment_start_time": datetime.strptime(str(args["assignment_start_time"]), '%B %d, %Y %H:%M:%S'),
            "assignment_end_time": datetime.strptime(str(args["assignment_end_time"]), '%B %d, %Y %H:%M:%S'),
        }
        insert_status = insert_one_document(mongo.db.assignments, new_assignment)
        return {
            "success": insert_status,
            "assignment_id": assignment_id,
        } if insert_status else {"success": insert_status}

    @api.doc(parser=modify_assignment_parser)
    @api.expect(modify_assignment_req)
    @api.marshal_with(modify_assignment_res)
    def patch(self):
        try:
            args = modify_assignment_parser.parse_args()
            assignment_id = str(args["assignment_id"])
            assignment_filter = {"assignment_id": assignment_id}
            update_assignment = {'$set': {
                "assignment_name": str(args["assignment_name"]),
                "assignment_start_time": datetime.strptime(str(args["assignment_start_time"]), '%B %d, %Y %H:%M:%S'),
                "assignment_end_time": datetime.strptime(str(args["assignment_end_time"]), '%B %d, %Y %H:%M:%S'),
            }}
            update_status = update_one_document(mongo.db.assignments, assignment_filter, update_assignment)
            return {
                "success": update_status,
                "assignment_id": assignment_id,
            } if update_status else {"success": update_status}
        except Exception as e:
            print("An exception occurred ::", e)
        return {"success": False}

    @api.doc(parser=delete_assignment_parser)
    @api.expect(delete_assignment_req)
    @api.marshal_with(modify_assignment_res)
    def delete(self):
        args = delete_assignment_parser.parse_args()
        assignment_id = str(args["assignment_id"])
        delete_status = delete_many_document(mongo.db.assignments, {"assignment_id": assignment_id})
        # delete questions inside
        delete_status = delete_many_document(mongo.db.questions,
                                             {"assignment_id": assignment_id}) and delete_status
        return {
            "success": delete_status,
            "assignment_id": assignment_id,
        } if delete_status else {"success": delete_status}


@login_required
@api.route("/QuestionListQuery")
class QuestionListQuery(Resource):
    @api.marshal_with(question_list_res)
    def get(self):
        questions = list(mongo.db.questions.find({}))
        return [{
            "question_id": str(i["question_id"]),
            "question_name": str(i["question_name"]),
        } for i in questions]


post_question_parser = api.parser()
post_question_parser.add_argument(
    "question_name", type=str, required=True,
)
post_question_parser.add_argument(
    "question_description", type=str, required=True,
)

post_question_parser.add_argument(
    "question_output", type=str, required=True,
)
post_question_parser.add_argument(
    "assignment_id", type=str, required=True,
)
post_question_parser.add_argument(
    "db_id", type=str, required=True,
)

modify_question_parser = api.parser()
modify_question_parser.add_argument(
    "question_id", type=str, required=True,
)
modify_question_parser.add_argument(
    "question_name", type=str, required=True,
)
modify_question_parser.add_argument(
    "question_description", type=str, required=True,
)

modify_question_parser.add_argument(
    "question_output", type=str, required=True,
)
modify_question_parser.add_argument(
    "assignment_id", type=str, required=True,
)
modify_question_parser.add_argument(
    "db_id", type=str, required=True,
)

delete_question_parser = api.parser()
delete_question_parser.add_argument(
    "question_id", type=str, required=True,
)


@login_required
@api.route("/QuestionDetail")
class QuestionDetail(Resource):
    @api.doc(parser=delete_question_parser)
    @api.expect(delete_question_req)
    @api.marshal_with(get_question_res)
    def get(self):
        args = delete_question_parser.parse_args()
        q = mongo.db.questions.find_one({"question_id": args["question_id"]})
        # print(q)
        return {
            'question_id': str(q["question_id"]),
            'question_name': str(q["question_name"]),
            'question_description': str(q["question_description"]),
            'question_output': str(q["question_output"]),
            'assignment_id': str(q["assignment_id"]),
            'db_id': str(q["db_id"]),
        }

    @api.doc(parser=post_question_parser)
    @api.expect(add_question_req)
    @api.marshal_with(modify_question_res)
    def post(self):
        args = post_question_parser.parse_args()
        question_id = "q-" + str(ObjectId())
        new_question = {
            "question_id": question_id,
            "question_name": str(args["question_name"]),
            "assignment_id": str(args["assignment_id"]),
            "question_output": str(args["question_output"]),
            "question_description": str(args["question_description"]),
            "db_id": str(args["db_id"]),
        }
        insert_status = insert_one_document(mongo.db.questions, new_question)
        return {
            "success": insert_status,
            "question_id": question_id,
        } if insert_status else {"success": insert_status}

    @api.doc(parser=modify_question_parser)
    @api.expect(modify_question_req)
    @api.marshal_with(modify_question_res)
    def patch(self):
        args = modify_question_req.parse_args()
        question_id = str(args["question_id"])
        assignment_filter = {"question_id": question_id}
        update_question = {'$set': {
            "question_name": str(args["assignment_name"]),
            "question_description": str(args["question_description"]),
            "question_output": str(args["question_output"]),
            "assignment_id": str(args["assignment_id"]),
            "db_id": str(args["db_id"]),
        }}
        update_status = update_one_document(mongo.db.questions, assignment_filter, update_question)
        return {
            "success": update_status,
            "question_id": question_id,
        } if update_status else {"success": update_status}

    @api.doc(parser=delete_question_parser)
    @api.expect(delete_question_req)
    @api.marshal_with(modify_question_res)
    def delete(self):
        args = delete_question_parser.parse_args()
        question_id = str(args["question_id"])
        delete_status = delete_many_document(mongo.db.questions, {"question_id": question_id})
        return {
            "success": delete_status,
            "question_id": question_id,
        } if delete_status else {"success": delete_status}


@login_required
@api.route("/DatabaseListQuery")
class DatabaseListQuery(Resource):
    @api.marshal_with(db_list_res)
    def get(self):
        dbs = list(mongo.db.db.find({}))
        return [{
            "db_id": str(i["db_id"]),
            "db_name": str(i["db_name"]),
            "upload_time": i["upload_time"].strftime('%B %d, %Y %H:%M:%S'),
        } for i in dbs]


post_db_parser = api.parser()
post_db_parser.add_argument(
    "db_name", type=str, required=True,
)
post_db_parser.add_argument(
    "db_description", type=str, required=False,
)

modify_db_parser = api.parser()
modify_db_parser.add_argument(
    "db_id", type=str, required=True,
)
modify_db_parser.add_argument(
    "db_name", type=str, required=True,
)
post_db_parser.add_argument(
    "db_description", type=str, required=False,
)

delete_db_parser = api.parser()
delete_db_parser.add_argument(
    "db_id", type=str, required=True,
)


@login_required
@api.route("/DatabaseDetail")
class DatabaseDetail(Resource):
    @api.doc(parser=delete_db_parser)
    @api.expect(delete_db_req)
    @api.marshal_with(db_detail_full_res)
    def get(self):
        args = delete_question_parser.parse_args()
        q = mongo.db.dbs.find_one({"db_id": args["db_id"]})
        # print(q)
        return {
            'db_id': str(q["db_id"]),
            'db_name': str(q["db_name"]),
            "db_description": str(q["db_description"]),
            "db_filename": str(q["db_filename"]),
            'upload_time': datetime.strptime(str(q["upload_time"]), '%B %d, %Y %H:%M:%S'),
        }

    @api.doc(parser=post_db_parser)
    @api.expect(add_db_req)
    @api.marshal_with(modify_db_res)
    def post(self):
        args = post_db_parser.parse_args()
        # todo handle file
        file = upload_parser.parse_args()['file']
        filename = str(ObjectId())
        db_id = "db-" + filename
        new_db = {
            "db_id": db_id,
            "db_name": str(args["db_name"]),
            "db_description": str(args["db_description"]),
            "db_filename": filename,
            "upload_time": datetime.now(),
        }
        insert_status = insert_one_document(mongo.db.db, new_db)
        # todo check file creation
        return {
            "success": insert_status,
            "db_id": db_id,
        } if insert_status else {"success": insert_status}

    @api.doc(parser=modify_db_parser)
    @api.expect(modify_db_req)
    @api.marshal_with(modify_db_res)
    def patch(self):
        args = modify_db_parser.parse_args()
        # todo handle file
        file = upload_parser.parse_args()['file']
        db_id = str(args["db_id"])
        db_filter = {"db_id": db_id}
        update_db = {'$set': {
            "db_name": str(args["db_name"]),
            "db_description": str(args["db_description"]),
            "upload_time": datetime.now(),
        }}
        update_status = update_one_document(mongo.db.dbs, db_filter, update_db)
        return {
            "success": update_status,
            "db_id": db_id,
        } if update_status else {"success": update_status}

    @api.doc(parser=delete_db_parser)
    @api.expect(delete_db_req)
    @api.marshal_with(modify_db_res)
    def delete(self):
        args = delete_db_parser.parse_args()
        db_id = str(args["db_id"])
        if mongo.db.dbs.find_one({"db_id": db_id}) is not None:
            delete_status = False
        else:
            delete_status = delete_many_document(mongo.db.dbs, {"db_id": db_id})
        return {
            "success": delete_status,
            "db_id": db_id,
        } if delete_status else {"success": delete_status}
