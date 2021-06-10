from flask_restx import Namespace, Resource, fields

import bcrypt
from flask_login import login_required, login_user, logout_user, current_user
from .extension import mongo, login_manager, User

from .model import *
from .util import update_one_document

api = Namespace('login', description='User login request')

login_res = api.model('Login', login_res_model)

cp_res = api.model('Login',
                   {'success': fields.Boolean(required=True, description="Change status")})

parser = api.parser()
parser.add_argument(
    "username", type=str, required=True, help="Username"
)
parser.add_argument(
    "password", type=str, required=True, help="Raw password"
)


@login_manager.user_loader
def user_loader(username):
    dbuser = mongo.db.users.find_one({"username": username})
    if dbuser is None:
        return

    user = User(username, dbuser["role"])
    return user


@login_manager.request_loader
def request_loader(request):
    username = request.form.get('username')
    dbuser = mongo.db.users.find_one({"username": username})
    if dbuser is None:
        return

    user = User(username, dbuser["role"])
    if bcrypt.checkpw(
            bytes(request.form['password'], encoding='utf8'),
            dbuser["password"]):
        return user

    return None


@api.route("login")
class Login(Resource):
    @api.doc(parser=parser)
    @api.marshal_with(login_res)
    def post(self):
        args = parser.parse_args()
        # print("args" + str(args))
        dbuser = mongo.db.users.find_one({"username": args["username"]})
        if dbuser is not None:
            # print(dbuser)
            # print(args["password"], dbuser["password"])
            if bcrypt.checkpw(bytes(args["password"], encoding='utf8'), dbuser["password"]):
                user = User(args["username"], dbuser["role"])
                login_user(user)
                return {"success": True, "data": dbuser["role"]}

        return {"success": False, "data": ""}


@api.route("register")
class Register(Resource):
    @api.doc(parser=parser)
    @api.marshal_with(login_res)
    def post(self):
        args = parser.parse_args()
        # print("args" + str(args))
        dbuser = mongo.db.users.find_one({"username": args["username"]})
        if dbuser is None:
            newuser = {
                "username": args["username"],
                "password": bcrypt.hashpw(bytes(args["password"], encoding='utf8'), bcrypt.gensalt()),
                "role": "student"
            }
            mongo.db.users.insert_one(newuser)
            user = User(args["username"], "student")
            login_user(user)
            return {"success": True, "data": "student"}
        return {"success": False, "data": ""}


cp_parser = api.parser()
cp_parser.add_argument(
    "current_pw", type=str, required=True, help="Current password"
)
cp_parser.add_argument(
    "new_pw", type=str, required=True, help="New password"
)


@login_required
@api.route("ChangePassword")
@api.doc(description="Change current user's password")
class ChangePassword(Resource):
    @api.doc(parser=cp_parser)
    @api.marshal_with(cp_res, as_list=True)
    def get(self):
        args = cp_parser.parse_args()
        dbuser = mongo.db.users.find_one({"username": current_user.id})
        if dbuser is not None and \
                bcrypt.checkpw(bytes(args["current_pw"], encoding='utf8'), dbuser["password"]):
            status = update_one_document(mongo.db.users,
                                         {"username": current_user.id},
                                         {"$set":
                                             {
                                                 "password":
                                                     bcrypt.hashpw(bytes(args["new_pw"], encoding='utf8'),
                                                                   bcrypt.gensalt())
                                             }
                                         })
            if status:
                return {"success": True}
        return {"success": False}
