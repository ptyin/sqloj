from datetime import datetime

from flask import Flask, Blueprint
from flask_restx import Api
import bcrypt

from .extension import mongo, login_manager

from .login import api as login_api
from .student import api as student_api
from .teacher import api as teacher_api


def create_app():
    app = Flask(__name__)
    app.secret_key = 'dev'
    app.config["MONGO_URI"] = "mongodb://localhost:27017/sqloj_db"
    # initialize extensions
    mongo.init_app(app, uri=app.config["MONGO_URI"])
    # for test
    if mongo.db.users.count() == 0:
        newuser = {
            "username": "admin",
            "password": bcrypt.hashpw(b"admin", bcrypt.gensalt()),
            "role": "teacher"
        }
        mongo.db.users.insert_one(newuser)
    if mongo.db.assignments.count() == 0:
        new_assignments = [
            {
                "assignment_id": "a-066ab87a062b",
                "assignment_name": "第一次作业",
                "assignment_start_time": datetime.strptime('June 13, 2021 11:13:00', '%B %d, %Y %H:%M:%S'),
                "assignment_end_time": datetime.strptime("June 20, 2021 23:59:59", '%B %d, %Y %H:%M:%S')
            },
            {
                "assignment_id": "a-204urhiugr9r",
                "assignment_name": "第二次作业",
                "assignment_start_time": datetime.strptime("June 20, 2021 12:47:00", '%B %d, %Y %H:%M:%S'),
                "assignment_end_time": datetime.strptime("June 27, 2021 23:59:59", '%B %d, %Y %H:%M:%S'),
            },
            {
                "assignment_id": "a-2ugr9r4urh6i",
                "assignment_name": "第三次作业",
                "assignment_start_time": datetime.strptime("June 30, 2021 23:59:59", '%B %d, %Y %H:%M:%S'),
                "assignment_end_time": datetime.strptime("July 5, 2021 23:59:59", '%B %d, %Y %H:%M:%S'),
            },
            {
                "assignment_id": "a-2ug1234urh6i",
                "assignment_name": "第四次作业",
                "assignment_start_time": datetime.strptime("June 30, 2021 23:59:59", '%B %d, %Y %H:%M:%S'),
                "assignment_end_time": datetime.strptime("July 5, 2021 23:59:59", '%B %d, %Y %H:%M:%S'),
            },
        ]
        mongo.db.assignments.insert_many(new_assignments)
    if mongo.db.questions.count() == 0:
        new_questions = [
            {
                "question_id": "q-21ru2933hui4",
                "question_name": "7-1 select查询",
                "assignment_id": "a-066ab87a062b",
                "question_description": "2",
                "question_output": "3333333333",
                "question_answer": "select * from albums;",
                "question_standard_output":[],
                'db_id': "db-21ru2933hui4"
            },
            {
                "question_id": "q-r9imvrvnq40s",
                "question_name": "7-3 delete删除",
                "assignment_id": "a-066ab87a062b",
                "question_description": "234",
                "question_output": "3333333333",
                "question_answer": "select * from albums;",
                "question_standard_output": [],
                'db_id': "db-r9imvrvnq40s"
            }
        ]
        mongo.db.questions.insert_many(new_questions)
    if mongo.db.records.count() == 0:
        new_records = [
            {
                "record_id": "r-21ru2933hui4",
                "question_id": "q-21ru2933hui4",
                "assignment_id": "a-066ab87a062b",
                "username": "admin",
                "submit_time": datetime.fromisoformat('2021-05-04 00:05:23.283'),
                "finished_time": datetime.fromisoformat('2021-05-07 00:05:23.283'),
                "record_code": "code1",
                "record_status": "AC",
                "running_time": 100,
                "record_lack": 0,
                "record_err": 0,
            },
            {
                "record_id": "r-21ru2923hui4",
                "question_id": "q-21ru2933hui4",
                "assignment_id": "a-066ab87a062b",
                "username": "admin",
                "submit_time": datetime.fromisoformat('2021-05-04 00:05:23.283'),
                "finished_time": datetime.fromisoformat('2021-05-07 00:05:23.283'),
                "record_code": "code1",
                "record_status": "AC",
                "running_time": 100,
                "record_lack": 0,
                "record_err": 0,
            },
            {
                "record_id": "r-21ru2943hui4",
                "question_id": "q-21ru2933hui4",
                "assignment_id": "a-066ab87a062b",
                "username": "123",
                "submit_time": datetime.fromisoformat('2021-05-04 00:05:23.283'),
                "finished_time": datetime.fromisoformat('2021-05-07 00:05:23.283'),
                "record_code": "code1",
                "record_status": "AC",
                "running_time": 100,
                "record_lack": 0,
                "record_err": 0,
            },
            {
                "record_id": "r-r9imvrvnq40s",
                "question_id": "q-r9imvrvnq40s",
                "assignment_id": "a-066ab87a062b",
                "username": "123",
                "submit_time": datetime.fromisoformat('2021-05-06 00:05:23.283'),
                "finished_time": datetime.fromisoformat('2021-05-07 00:05:23.283'),
                "record_code": "code2",
                "record_status": "RUNNING",
                "running_time": 0,
                "record_lack": 0,
                "record_err": 0,
            }
        ]
        mongo.db.records.insert_many(new_records)
    if mongo.db.record_outputs.count() == 0:
        new_dbs = [
            {
                "question_id": "q-21ru2933hui4",
                "username": "admin",
                "output": "123456",
                "record_id": "r-21ru2923hui4",
                "submit_time": datetime.fromisoformat('2021-05-04 00:05:23.283'),
                "finished_time": datetime.fromisoformat('2021-05-07 00:05:23.283'),
            },
            {
                "question_id": "q-21ru2933hui4",
                "username": "123",
                "output": "lalalala",
                "record_id": "r-21ru2943hui4",
                "submit_time": datetime.fromisoformat('2021-05-04 00:05:23.283'),
                "finished_time": datetime.fromisoformat('2021-05-08 00:05:23.283'),
            },
        ]
        mongo.db.record_outputs.insert_many(new_dbs)
    if mongo.db.dbs.count() == 0:
        new_dbs = [
            {
                "db_id": "db-21ru2933hui4",
                "db_name": "q1",
                "db_description": "123",
                "db_filename": "21ru2933hui4",
                "upload_time": datetime.fromisoformat('2021-05-06 00:05:23.283'),
            },
            {
                "db_id": "db-r9imvrvnq40s",
                "db_name": "q2",
                "db_description": "123",
                "db_filename": "r9imvrvnq40s",
                "upload_time": datetime.fromisoformat('2021-05-06 00:05:23.283'),
            },
        ]
        mongo.db.dbs.insert_many(new_dbs)
    login_manager.init_app(app)

    # initialize modules
    api_blueprint = Blueprint('api', __name__, url_prefix='/api')
    api = Api(api_blueprint)
    app.register_blueprint(api_blueprint)
    api.add_namespace(login_api, path='/login')
    api.add_namespace(student_api, path='/student')
    api.add_namespace(teacher_api, path='/teacher')

    return app
