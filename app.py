import os
from flask import Flask
app = Flask(_name_)

@app.route("/")
def main():
    return "Welcome!"

@app.route('/how are you')
def hello():
    return 'I am good, how about you?'

if _name_ == "_main_":
    app.run(host="0.0.0.0", port=8090)
