import keyboard  # using module keyboard

while True:
    if keyboard.read_key() == "w":
        print("walk forward")
    if keyboard.read_key() == "s":
        print("walk backward")
    if keyboard.read_key() == "d":
        print("turn right")
    if keyboard.read_key() == "a":
        print("turn left")
