import pathlib
from datetime import datetime

from .module.judge import get_answer, judge
from .util import update_one_document, insert_one_document

from .extension import mongo

folder = pathlib.Path(__file__).parent


def update_question_standard_output(qids):
    # print(qids)
    for qid in qids:
        question = mongo.db.questions.find_one({"question_id": qid})
        # print(question, qid)
        header, output, _ = get_answer(folder / "database" / question["db_id"][3:], str(question["question_answer"]), )
        update_one_document(mongo.db.questions, {"question_id": qid}, {
            "$set":
                {
                    "question_standard_header": header,
                    "question_standard_output": output
                }
        })
    print(str(qids) + " standard update finished")


def get_user_answer_output(rec_id, sql, qid, submit_time, username):
    question = mongo.db.questions.find_one({"question_id": qid})
    standard_output = question["question_standard_output"]
    standard_header = question["question_standard_header"]
    # print((standard_header, standard_output))
    # print("")
    # after getting output
    status, (lack_num, err_num), (header, output), running_time = judge(folder / "database" / question["db_id"][3:],
                                                                        sql, standard_header, standard_output)
    # print(status, (lack_num, err_num), (header, output), running_time)
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
    # doc_filter = {
    #     "username": username,
    #     "question_id": qid
    # }
    # print(header)
    # print(output)
    # if mongo.db.record_outputs.find_one(doc_filter) is None:
    #     new_output = {
    #         "question_id": qid,
    #         "username": username,
    #         "record_header": header,
    #         "record_output": output,
    #         "record_id": rec_id,
    #         "submit_time": submit_time,
    #         "finished_time": finished_time
    #     }
    #     # print("w")
    #     insert_one_document(mongo.db.record_outputs, new_output)
    # else:
    #     doc_update = {"$set":
    #         {
    #             "record_header": header,
    #             "record_output": output,
    #             "record_id": rec_id,
    #             "submit_time": submit_time,
    #             "finished_time": finished_time
    #         }
    #     }
    #     # print("w2")
    #     update_one_document(mongo.db.record_outputs, doc_filter, doc_update)

    new_output = {
        "question_id": qid,
        "username": username,
        "record_header": header,
        "record_output": output,
        "record_id": rec_id,
        "submit_time": submit_time,
        "finished_time": finished_time
    }
    # print("w")
    insert_one_document(mongo.db.record_outputs, new_output)
    print(rec_id + " user code execution finished")
