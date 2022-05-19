from flask import Flask
from flask import request
import json

server = Flask(__name__)


@server.route('/')
def getWelcome():
    return "IMAGE TEST SERVER"


@server.route('/image')
def getImage():
    with open('Images.json', 'r') as openfile:
        jsonList = json.load(openfile)
    imageList = json.dumps(jsonList)
    return imageList


def updateImageFile(newImage, filename='Images.json'):
    with open(filename, 'r+') as file:
        images = json.load(file)
        images.append(newImage)
        file.seek(0)
        json.dump(images, file, indent=4)


@server.route('/post', methods=["POST"])
def postImage():
    value = request.json
    updateImageFile(value)
    return 'Image Saved'


server.run(host='0.0.0.0')
