from flask_pymongo import PyMongo

mongo = PyMongo()
db = mongo.db

from flask_login import LoginManager, UserMixin


class User(UserMixin):
    def __init__(self, uid, role):
        self.id = uid
        self.role = role


login_manager = LoginManager()
