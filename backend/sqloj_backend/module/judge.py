# author: wei ziyao

import pandas as pd
import threading
import sys
import sqlite3
import time


# command = 'sqlite3 -header -csv test.db "{sql}"'
class Dispacher(threading.Thread):
    def __init__(self, fun, args):
        threading.Thread.__init__(self)
        self.setDaemon(True)
        self.dataframe = pd.DataFrame()
        self.state = ""
        self.running_time = 0
        self.error = None
        self.fun = fun
        self.args = args
        self.start()

    def run(self):
        try:
            self.dataframe, self.running_time, self.state = self.fun(*self.args)
        except Exception as e:
            print("An exception occurred ::", e)
            self.error = sys.exc_info()
            print(self.error, self.args)


def run_sql(database, sql, name):
    conn = sqlite3.connect(database=database)
    cursor = conn.cursor()
    result = cursor.execute(sql)
    print("11111111111111", sql, cursor.description)
    columns_tuple = cursor.description
    columns_name = ""
    for field_tuple in columns_tuple:
        columns_name += field_tuple[0] + ","
    print(columns_name)
    running_time = time.thread_time_ns()

    # result = str(result, encoding="utf-8")
    with open('tmp' + name + '.csv', 'wt') as temp:
        temp.writelines(columns_name)
        for row in result:
            size = len(row)
            row_res = ""
            for i in range(size):
                row_res = row_res + row[i] + ","
            temp.writelines(row_res)
    dataframe = pd.DataFrame()
    try:
        dataframe: pd.DataFrame = pd.read_csv('tmp.csv')
        state = "F"
    except:
        state = "RE"
    return dataframe, running_time, state


def standard_sql_output(sql, database, name):
    c = Dispacher(run_sql, (database, sql, name))
    c.join(3)
    # we assume it executes successfully
    dataframe = c.dataframe
    columns_name = c.columns_name
    print(result, columns_name)
    # result = str(result, encoding="utf-8")
    with open('tmp' + name + '.csv', 'wt') as temp:
        temp.writelines(columns_name)
        for row in result:
            size = len(row)
            row_res = ""
            for i in range(size):
                row_res = row_res + row[i] + ","
            temp.writelines(row_res)
    dataframe: pd.DataFrame = pd.read_csv('tmp.csv')
    return dataframe


def judge(sql, answer: pd.DataFrame, database, name):
    conn = sqlite3.connect(database=database)
    cursor = conn.cursor()
    c = Dispacher(run_sql, (cursor, sql))
    c.join(3)
    if c.is_alive():
        return "TLE", 0, 0, 0, None
    result = c.result
    columns_name = c.columns_name
    running_time = c.running_time
    # result = str(result, encoding="utf-8")
    with open('tmp' + name + '.csv', 'wt') as temp:
        temp.writelines(columns_name)
        for row in result:
            size = len(row)
            row_res = ""
            for i in range(size):
                row_res = row_res + row[i] + ","
            temp.writelines(row_res)
    try:
        dataframe: pd.DataFrame = pd.read_csv('tmp.csv')
    except:
        return "RE", running_time, 0, 0, None
    # judge
    ordered_dataframe, ordered_answer = [], []
    for sub_lis in dataframe:
        ordered_dataframe.append(sorted(sub_lis, key=lambda x: x[0]))
    for sub_lis in answer:
        ordered_answer.append(sorted(sub_lis, key=lambda x: x[0]))
    ordered_answer = sorted(ordered_answer)
    ordered_dataframe = sorted(ordered_dataframe)
    ans_len, data_len = len(ordered_answer), len(ordered_dataframe)
    ans_cnt, data_cnt = 0, 0
    corr_num = 0
    while True:
        if data_cnt >= data_len or ans_cnt >= ans_len:
            break
        if ordered_answer[ans_cnt] == ordered_dataframe[data_cnt]:
            corr_num += 1
            ans_cnt += 1
            data_cnt += 1
        else:
            if ordered_dataframe[data_cnt] < ordered_answer[ans_cnt]:
                data_cnt += 1
            else:
                ans_cnt += 1
    lack_num = ans_len - corr_num
    err_num = data_len - corr_num
    if lack_num == 0 and err_num == 0:
        return "AC", running_time, 0, 0, dataframe
    else:
        return "WA", running_time, lack_num, err_num, dataframe


if __name__ == '__main__':
    judge('select * from COMPANY;', None, 'db33387c4a88', "adfsd")
