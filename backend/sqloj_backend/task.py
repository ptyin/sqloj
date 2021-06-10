import pathlib
from datetime import datetime

from .module.judge import standard_sql_output, judge
from .util import update_one_document, insert_one_document

from .extension import mongo

folder = pathlib.Path(__file__).parent


def update_question_standard_output(qids):
    # print(qids)
    for qid in qids:
        question = mongo.db.questions.find_one({"question_id": qid})
        # print(question, qid)
        output = standard_sql_output(str(question["question_answer"]), folder / "database" / question["db_id"][3:], qid)
        update_one_document(mongo.db.questions, {"question_id": qid}, {
            "$set":
                {
                    "question_standard_output": [output]
                }
        })
    print(qids + " standard update finished")


def get_user_answer_output(rec_id, sql, qid, submit_time, username):
    question = mongo.db.questions.find_one({"question_id": qid})
    standard_output = question["question_standard_output"]
    # after getting output
    status, running_time, lack_num, err_num, output = judge(sql, standard_output,
                                                            folder / "database" / question["db_id"][3:], rec_id)
    finished_time = datetime.now()
    update_one_document(mongo.db.records, {"record_id": rec_id}, {
        "$set":
            {
                "record_status": status,
                "running_time": running_time,
                "record_lack": lack_num,
                "record_err": err_num,
                "finished_time": finished_time
            }
    })
    doc_filter = {
        "username": username,
        "question_id": qid
    }
    if mongo.db.record_outputs.find_one(doc_filter) is None:
        new_output = {
                         "question_id": qid,
                         "username": username,
                         "output": output,
                         "record_id": rec_id,
                         "submit_time": submit_time,
                         "finished_time": finished_time
                     },
        insert_one_document(mongo.db.record_outputs, new_output)
    else:
        doc_update = {"$set",
                      {
                          "output": output,
                          "record_id": rec_id,
                          "submit_time": submit_time,
                          "finished_time": finished_time
                      },
                      }
        update_one_document(mongo.db.record_outputs, doc_filter, doc_update)

    print(rec_id + " user code execution finished")
