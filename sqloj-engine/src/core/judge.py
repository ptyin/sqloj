# author: wei ziyao


import sqlite3
import threading
import sys
import time
from math import floor


class Dispatcher(threading.Thread):
    def __init__(self, fun, args):
        threading.Thread.__init__(self)
        self.setDaemon(True)
        self.out = None
        self.out_header = None
        self.running_time = 233
        self.error = None
        self.fun = fun
        self.args = args
        self.start()

    def run(self):
        try:
            self.out_header, self.out, self.running_time = self.fun(*self.args)
        except:
            self.error = sys.exc_info()


def get_answer(database, sql):
    start_time = time.time()
    conn = sqlite3.connect(database=database)
    cursor = conn.cursor()
    sql_list = sql.split(";")
    if sql_list[-1] == '':
        sql_list = sql_list[0:-1]
    sql_num = len(sql_list)
    for i in range(sql_num - 1):
        cursor.execute(sql_list[i])
    cursor.execute(sql_list[sql_num - 1])
    a = 0
    for i in range(2000):
        a += i
    stop_time = time.time()
    running_time = stop_time - start_time
    out = cursor.fetchall()
    cursor.close()
    conn.close()
    out_header = []
    for field_tuple in cursor.description:
        out_header.append(field_tuple[0])
    return out_header, out, running_time


def sortout(out_header, out):
    corr_index = []
    out_header_len = len(out_header)
    sort_out_header = out_header.copy()
    sort_out_header.sort()
    for col_name in sort_out_header:
        for i in range(out_header_len):
            if col_name == out_header[i]:
                corr_index.append(i)
                break
    sort_out = []
    for row in out:
        tmp = []
        for i in range(out_header_len):
            tmp.append(row[corr_index[i]])
        sort_out.append(tuple(tmp))
    return sort_out


def judge(database, sql, answer_header, answer_output):
    c = Dispatcher(get_answer, (database, sql))
    c.join(3)
    if c.is_alive():
        return "TLE", (0, 0), (None, None), 3000
    if c.error is not None:
        print(c.error)
        return "RE", (0, 0), (None, None), 3000
    out_header, out = c.out_header, c.out
    running_time = floor(c.running_time * 1000)
    # use 20 ms because thread_time() has usually a resolution of 15 ms
    running_time = 20 if running_time < 20 else running_time

    sort_out = sortout(out_header, out)
    answer_out = sortout(answer_header, answer_output)
    corr_row_num, error_row_num = 0, 0
    out_cnt, ans_cnt = 0, 0
    out_len, ans_len = len(sort_out), len(answer_out)
    while True:
        if out_cnt >= out_len or ans_cnt >= ans_len:
            break
        if answer_out[ans_cnt] == sort_out[out_cnt]:
            corr_row_num += 1
            ans_cnt += 1
            out_cnt += 1
        else:
            if sort_out[out_cnt] < answer_out[ans_cnt]:
                out_cnt += 1
            else:
                ans_cnt += 1
    lack_num = ans_len - corr_row_num
    err_num = out_len - corr_row_num
    if lack_num == 0 and err_num == 0:
        return "AC", (0, 0), (out_header, out), running_time
    else:
        return "WA", (lack_num, err_num), (out_header, out), running_time

