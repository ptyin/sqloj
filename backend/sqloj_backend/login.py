from flask_restx import Namespace, Resource, fields

import bcrypt
from flask_login import login_required, login_user, logout_user
from .extension import mongo, login_manager, User

from .model import *
api = Namespace('login', description='User login request')

login_res = api.model('Login', login_res_model)

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


@api.route("/login")
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

        # TODO for develop, add user if not exists
        else:
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
