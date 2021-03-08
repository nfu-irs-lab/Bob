

def Robotis(id, pos, speed):
    ser = serial.Serial('/dev/ttyUSB0',  57142, timeout= 0.5);
    ser.bytesize = serial.EIGHTBITS
    arr =[ ]
    arr.append(0xff)
    arr.append(0xff)
    arr.append(id)
    arr.append(0x07)
    arr.append(0x03)
    arr.append(0x1e)
    arr.append(pos & 255)
    arr.append(pos // 256)
    arr.append(speed & 255)
    arr.append(speed // 256)
    tt = 0xff - (sum(arr[2:9]) & 255 )
    arr.append(tt)
    ary = bytearray(arr)
    ser.write(ary)
