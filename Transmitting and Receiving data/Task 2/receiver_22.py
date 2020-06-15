#!/usr/bin/python

import time
import sys
import serial

ser = serial.Serial(port='/dev/ttyACM0', baudrate=115200)

bitSendTime = 10
byteSendTime = 200

# convert a binary array to a char
def arrayToLetter(binaryArray):
    output = 0
    for b in binaryArray:
        output = (output << 1) | b
    return chr(output)

def analogToDigital(analogValue):
    if analogValue == '':
      return 0
    if int(analogValue) < 300:
      return 0
    else:
      return 1

while True:
        currentTime = time.time()
        milliseconds = ((currentTime * 1000) % byteSendTime)

        #if True:
        #if milliseconds < 10
        if milliseconds > 0 and  milliseconds < 1:
                ser.flushInput()
                binaryArrayOne = []
                binaryArrayTwo = []
                currentState = 0

                # start receiving
                for i in range(0, 8):
                    chipArray = []
                    for j in range(0, 2):
                        
                        line = ser.readline()
                        out = line.rstrip()
                        #print out
                        #print milliseconds
                        chipArray.append(analogToDigital(out))
                    
                        # waiting
                        #currentState = currentState + 1
                        #while milliseconds < currentState * bitSendTime + 25:
                        #    currentTime = time.time()
                        #    milliseconds = ((currentTime * 1000) % byteSendTime)
                    
                    #print chipArray
                    if chipArray[0] == 0 and chipArray[1] == 0:
                      binaryArrayOne.append(0)
                      binaryArrayTwo.append(0)
                    if chipArray[0] == 1 and chipArray[1] == 0:
                      binaryArrayOne.append(1)
                      binaryArrayTwo.append(0)
                    if chipArray[0] == 0 and chipArray[1] == 1:
                      binaryArrayOne.append(0)
                      binaryArrayTwo.append(1)
                    if chipArray[0] == 1 and chipArray[1] == 1:
                      binaryArrayOne.append(1)
                      binaryArrayTwo.append(1)
 
                     
#                print binaryArrayOne
#                print binaryArrayTwo
#               print arrayToLetter(binaryArray)
                sys.stdout.write(arrayToLetter(binaryArrayOne))
                sys.stdout.write(arrayToLetter(binaryArrayTwo))
                sys.stdout.flush()







