# Currently a simple command line interface, will need to modify if we're adding a GUI

import requests

SERVER_URL = 'placeholder'

def join(username):
    response = requests.post(f'{SERVER_URL}/join', json={'username': username})
    print(response.json()['message'])

def leave(username):
    response = requests.post(f'{SERVER_URL}/leave', json={'username': username})
    print(response.json()['message'])

def post_message(sender, subject, content):
    response = requests.post(f'{SERVER_URL}/post', json={'sender': sender, 'subject': subject, 'content': content})
    print(response.json()['message'])

def get_message(message_id):
    response = requests.get(f'{SERVER_URL}/messages/{message_id}')
    message = response.json()
    print(f"Message ID: {message['id']}")
    print(f"Sender: {message['sender']}")
    print(f"Subject: {message['subject']}")
    print(f"Content: {message['content']}")

def get_users():
    response = requests.get(f'{SERVER_URL}/users')
    users = response.json()['users']
    print("Users in the group:")
    for user in users:
        print(user)

def main():
    username = input("Enter your username: ")
    join(username)
    while True:
        command = input("Enter command (%post, %message, %users, %leave, %exit): ")
        if command.startswith('%post'):
            _, subject, content = command.split(maxsplit=2)
            post_message(username, subject, content)
        elif command.startswith('%message'):
            _, message_id = command.split()
            get_message(int(message_id))
        elif command == '%users':
            get_users()
        elif command == '%leave':
            leave(username)
            break
        elif command == '%exit':
            leave(username)
            break
        else:
            print("Invalid command")

if __name__ == '__main__':
    main()