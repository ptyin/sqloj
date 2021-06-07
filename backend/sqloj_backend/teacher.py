from flask_restx import Namespace, Resource, fields

api = Namespace('login', description='User login request')

login_data = api.model('Model', {
    'username': fields.String,
    'password': fields.String
})


@api.route("/")
class Login(Resource):
    @api.marshal_with(login_data)
    def post(self):
        return {"success": True, "data": "student"}
