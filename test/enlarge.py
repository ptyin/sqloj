import sqlite3


if __name__ == '__main__':
    conn = sqlite3.connect('chinook_enlarged.db')
    c = conn.cursor()
    print("Opened database successfully")
    for i in range(348, 10000):
        c.execute("INSERT INTO albums (AlbumId,Title,ArtistId) \
          VALUES ({}, 'Test', 1);".format(i))

    cursor = c.execute("SELECT * from albums")
    for row in cursor:
        print(row)
    conn.close()
