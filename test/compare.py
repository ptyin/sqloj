import os
import time
import docker
import sqlite3
from docker.models.images import Image


# 先用python创建一个dockerfile
template = """FROM debian
RUN apt-get update && DEBIAN_FRONTEND=noninteractive apt-get -yq --no-install-recommends install sqlite3 && rm -rf /var/lib/apt/lists/* /tmp/* /var/tmp/* && mkdir -p /root/db
WORKDIR /root/db/
COPY {path} /root/db/
"""


def sqlite_to_image(path):
    client = docker.from_env()
    dockerfile = template.format(path=path)
    with open('dockerfile', 'wt') as file:
        file.write(dockerfile)
    # return client.images.build(path='./', dockerfile='dockerfile')
    image: Image = client.images.build(path='./', dockerfile='dockerfile', tag='tmp')[0]
    return image.id[7:]


def from_docker():
    client = docker.from_env()
    command = 'sqlite3 chinook.db "{sql}"'
    container = client.containers.run('tmp', command.format(sql="select * from albums;"))
    print(container)


if __name__ == '__main__':
    # start_time = time.time()
    # print(sqlite_to_image('chinook.db'))
    # print("build time: ", time.time()-start_time)
    start_time = time.time()
    from_docker()
    print("docker time: ", time.time()-start_time)
    start_time = time.time()
    os.popen('copy chinook.db chinook_tmp.db')
    con = sqlite3.connect('chinook_tmp.db')
    c = con.cursor()
    c.execute("select * from albums;")
    print("raw time: ", time.time()-start_time)
