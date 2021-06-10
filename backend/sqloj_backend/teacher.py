from flask_restx import Namespace, Resource
from flask_login import login_required, current_user

from bson.objectid import ObjectId
from werkzeug.datastructures import FileStorage

import os
import pathlib
from threading import Thread
from datetime import datetime

from .model import *
from .extension import mongo, login_manager
from .util import insert_one_document, update_one_document, delete_many_document
from .task import update_question_standard_output

folder = pathlib.Path(__file__).parent

api = Namespace('teacher', description="Students' manipulations")

assignment_list_res = api.model('Assignment detail', assignment_detail)

add_assignment_req = api.model('Added assignment detail', assignment_adding_detail)

modify_assignment_req = api.model('Modified assignment detail', assignment_detail)

delete_assignment_req = api.model("Delete assignment request", assignment_detail_req_model)

modify_assignment_res = api.model('Assignment adding status', add_assignment_res_model)

record_finished_req = api.model("Find finished student by question id",
                                record_by_qid_req_model)

question_student_status_res = api.model("Finished records of requested question",
                                        question_student_status_res_model)

record_output_req = api.model("Request record output by id", record_output_req_model)

record_output_res = api.model("Record output", record_output_res_model)

question_list_res = api.model('All questions', question_list)

add_question_req = api.model('Added question detail',
                             {**question_detail,
                              **{'question_answer': fields.String(required=True,
                                                                  description="Standard question answer"),
                                 'assignment_id': fields.String(
                                     required=True,
                                     description="Assignment id where the question lies"),
                                 'db_id': fields.String(
                                     required=True,
                                     description="Database id the question uses")}})

get_question_res = api.model('Question detail', question_detail_all_model)

modify_question_req = api.model('Modified question detail', question_detail_all_model)
delete_question_req = api.model("Delete question request", question_detail_req_model)
modify_question_res = api.model('Question adding status', add_question_res_model)

db_list_res = api.model('DB list', db_list_res_model)

db_detail_full_res = api.model('DB detail', db_detail_full_model)

add_db_req = api.model('Added DB detail', db_add_req_model)

modify_db_req = api.model('Modified DB detail', db_modify_req_model)

delete_db_req = api.model("Delete assignment request", db_delete_req_model)

modify_db_res = api.model('Assignment adding status', add_db_res_model)


# todo judge user role


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


question_id_parser = api.parser()
question_id_parser.add_argument("question_id", type=str, required=True)


@login_required
@api.route("/RecordListQuery")
class RecordListQuery(Resource):
    @api.doc(parser=question_id_parser)
    @api.expect(record_finished_req)
    @api.marshal_with(question_student_status_res, as_list=True)
    def get(self):
        try:
            args = question_id_parser.parse_args()
            question = mongo.db.questions.find_one({"question_id": args["question_id"], })
            if question is not None:
                qtype = question["question_type"]
                if qtype == "sql":
                    finishes_filter = {
                        "question_id": args["question_id"],
                        "record_status": "AC"
                    }
                    print(finishes_filter)
                    group_pipeline = {
                        "_id": "$username",
                        "submit_time": {"$last": "$submit_time"},
                        "record_id": {"$last": "$record_id"},
                    }
                    finishes = list(mongo.db.records.aggregate([
                        {"$match": finishes_filter},
                        {"$group": group_pipeline}
                    ]))
                    print(finishes)
                elif qtype == "text":
                    finishes_filter = {
                        "question_id": args["question_id"],
                    }
                    print(finishes_filter)
                    group_pipeline = {
                        "_id": "$username",
                        "submit_time": {"$last": "$submit_time"},
                        "record_id": {"$last": "$record_id"},
                    }
                    finishes = list(mongo.db.records.aggregate([
                        {"$match": finishes_filter},
                        {"$group": group_pipeline}
                    ]))
                    print(finishes)
            else:
                return [{
                    "record_id": None,
                    "username": None,
                    "submit_time": None
                }]
        except Exception as e:
            print("An exception occurred ::", e)
            return [{
                "record_id": None,
                "username": None,
                "submit_time": None
            }]
        return [{
            "record_id": str(finished["record_id"]),
            "username": str(finished["_id"]),
            "submit_time": finished["submit_time"].strftime('%B %d, %Y %H:%M:%S'),
        } for finished in finishes]


record_id_parser = api.parser()
record_id_parser.add_argument("record_id", type=str, required=True)


@login_required
@api.route("/RecordOutput")
class RecordOutput(Resource):
    @api.doc(parser=record_id_parser)
    @api.expect(record_output_req)
    @api.marshal_with(record_output_res)
    def get(self):
        args = record_id_parser.parse_args()
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


assignId_parser = api.parser()
assignId_parser.add_argument(
    "assignment_id", type=str, required=True, help="Requested assignment id"
)


@login_required
@api.route("/QuestionList")
@api.doc(description="Get question list of the requested assignment")
class QuestionListQuery(Resource):
    @api.doc(parser=assignId_parser)
    @api.expect(assignId_parser)
    @api.marshal_with(question_list_res, as_list=True)
    def get(self):
        args = assignId_parser.parse_args()
        questions = list(mongo.db.questions.find({"assignment_id": args["assignment_id"]}))
        return [{
            "question_id": str(i["question_id"]),
            "question_name": str(i["question_name"]),
        } for i in questions]


@login_required
@api.route("/QuestionListFull")
@api.doc(description="Get full question list")
class QuestionListFullQuery(Resource):
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
    "question_type", type=str, required=True,
)
post_question_parser.add_argument(
    "question_description", type=str, required=False,
)

post_question_parser.add_argument(
    "question_output", type=str, required=False,
)
post_question_parser.add_argument(
    "question_answer", type=str, required=False,
)

post_question_parser.add_argument(
    "assignment_id", type=str, required=True,
)
post_question_parser.add_argument(
    "db_id", type=str, required=False,
)

modify_question_parser = api.parser()
modify_question_parser.add_argument(
    "question_id", type=str, required=True,
)
modify_question_parser.add_argument(
    "question_name", type=str, required=True,
)
modify_question_parser.add_argument(
    "question_type", type=str, required=True,
)
modify_question_parser.add_argument(
    "question_description", type=str, required=False,
)
modify_question_parser.add_argument(
    "question_answer", type=str, required=False,
)
modify_question_parser.add_argument(
    "question_output", type=str, required=False,
)
modify_question_parser.add_argument(
    "assignment_id", type=str, required=True,
)
modify_question_parser.add_argument(
    "db_id", type=str, required=False,
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
            "question_type": str(q["question_type"]),
            'question_description': str(q["question_description"]),
            'question_output': str(q["question_output"]),
            'question_answer': q["question_answer"] if str(q["question_type"]) == "sql" else None,  # todo handle list
            'assignment_id': str(q["assignment_id"]),
            'db_id': str(q["db_id"]) if str(q["question_type"]) == "sql" else None,
        } if q is not None else {
        }

    @api.doc(parser=post_question_parser)
    @api.expect(add_question_req)
    @api.marshal_with(modify_question_res)
    def post(self):
        args = post_question_parser.parse_args()
        qtype = args["question_type"]
        question_id = "q-" + str(ObjectId())
        assignment = mongo.db.assignments.find_one({"assignment_id": str(args["assignment_id"])})
        if assignment is None:
            return {"success": False}
        if qtype == "sql":
            db = mongo.db.dbs.find_one({"db_id": str(args["db_id"])})
            if db is None:
                return {"success": False}
        new_question = {
            "question_id": question_id,
            "question_name": str(args["question_name"]),
            "question_type": qtype,
            "assignment_id": str(args["assignment_id"]),
            "question_answer": str(args["question_answer"]) if qtype == "sql" else None,
            "question_output": str(args["question_output"]),
            "question_description": str(args["question_description"]),
            "db_id": str(args["db_id"]) if qtype == "sql" else None,
        }
        insert_status = insert_one_document(mongo.db.questions, new_question)
        if qtype == "sql":
            Thread(target=update_question_standard_output,
                   args=[[question_id]]).start()
        return {
            "success": insert_status,
            "question_id": question_id,
        } if insert_status else {"success": insert_status}

    @api.doc(parser=modify_question_parser)
    @api.expect(modify_question_req)
    @api.marshal_with(modify_question_res)
    def patch(self):
        args = modify_question_parser.parse_args()
        qtype = args["question_type"]
        question_id = str(args["question_id"])
        assignment = mongo.db.assignments.find_one({"assignment_id": str(args["assignment_id"])})
        if assignment is None:
            return {"success": False}
        if qtype == "sql":
            db = mongo.db.dbs.find_one({"db_id": str(args["db_id"])})
            if db is None:
                return {"success": False}
        assignment_filter = {"question_id": question_id}
        update_question = {'$set': {
            "question_type": qtype,
            "question_name": str(args["assignment_name"]),
            "question_description": str(args["question_description"]),
            "question_answer": str(args["question_answer"]) if qtype == "sql" else None,
            "question_output": str(args["question_output"]),
            "assignment_id": str(args["assignment_id"]),
            "db_id": str(args["db_id"]) if qtype == "sql" else None,
        }}
        update_status = update_one_document(mongo.db.questions, assignment_filter, update_question)
        if qtype == "sql":
            Thread(target=update_question_standard_output,
                   args=[[question_id]]).start()
        return {
            "success": True,
            "question_id": question_id,
        } if update_status else {"success": False}

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
post_db_parser.add_argument('file', location='files',
                            type=FileStorage, required=True)

modify_db_parser = api.parser()
modify_db_parser.add_argument(
    "db_id", type=str, required=True,
)
modify_db_parser.add_argument(
    "db_name", type=str, required=True,
)
modify_db_parser.add_argument(
    "db_description", type=str, required=False,
)
modify_db_parser.add_argument('file', location='files',
                              type=FileStorage, required=False,
                              help="Send the file only if it is updated")

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
        filename = str(ObjectId())
        db_id = "db-" + filename
        new_db = {
            "db_id": db_id,
            "db_name": str(args["db_name"]),
            "db_description": str(args["db_description"]),
            "db_filename": filename,
            "upload_time": datetime.now(),
        }
        insert_status = insert_one_document(mongo.db.dbs, new_db)
        # check file creation
        file = args['file']
        if insert_status:
            try:
                file.save(folder / "database" / filename)
            except OSError as e:
                print("An exception occurred when saving file ::", e)
                delete_many_document(mongo.db.dbs, {"db_id": db_id})
                return {"success": False}
            return {
                "success": True,
                "db_id": db_id,
            }
        else:
            return {"success": False}

    @api.doc(parser=modify_db_parser)
    @api.expect(modify_db_req)
    @api.marshal_with(modify_db_res)
    def patch(self):
        args = modify_db_parser.parse_args()
        db_id = str(args["db_id"])
        db_filter = {"db_id": db_id}
        update_db = {'$set': {
            "db_name": str(args["db_name"]),
            "db_description": str(args["db_description"]),
            "upload_time": datetime.now(),
        }}
        update_status = update_one_document(mongo.db.dbs, db_filter, update_db)
        # check file overwrite
        filename = db_id[3:]
        file = args['file']
        if update_status:
            # the file may not be updated
            if file is not None:
                try:
                    file.save(folder / "database" / filename)
                except OSError as e:
                    print("An exception occurred when saving file ::", e)
                    return {"success": False}
            # update corresponding question answers
            qids = mongo.db.questions.distinct("question_id", {"db_id": db_id})
            Thread(target=update_question_standard_output,
                   args=[qids]).start()
            return {
                "success": True,
                "db_id": db_id,
            }
        else:
            return {"success": False}

    @api.doc(parser=delete_db_parser)
    @api.expect(delete_db_req)
    @api.marshal_with(modify_db_res)
    def delete(self):
        args = delete_db_parser.parse_args()
        db_id = str(args["db_id"])
        delete_status = False
        file_delete_status = False
        if mongo.db.questions.find_one({"db_id": db_id}) is None:
            db = mongo.db.dbs.find_one({"db_id": db_id})
            file_delete_status = True
            if db is not None:
                delete_status = delete_many_document(mongo.db.dbs, {"db_id": db_id})
                file_delete_status = False
                # keep trying deleting
                while delete_status:
                    try:
                        os.remove('/database/' + str(db["db_filename"]))
                    except Exception as e:
                        print("An exception occurred ::", e)
                        continue
                    file_delete_status = True
                    break
        return {
            "success": True,
            "db_id": db_id,
        } if delete_status and file_delete_status else {"success": False}
