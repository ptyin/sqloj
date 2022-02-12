import sqlite3


if __name__ == '__main__':
    database = 'chinook_tmp.db'
    conn = sqlite3.connect(database)
    cursor = conn.cursor()
    cursor.execute('drop table albums')
    conn.rollback()
    conn.close()
