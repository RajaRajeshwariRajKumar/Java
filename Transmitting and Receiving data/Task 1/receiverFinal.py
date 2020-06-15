  GNU nano 2.2.6                                                                  File: receiver.py

#!/usr/bin/python
import RPi.GPIO as GPIO
import time
import sys

# Use BCM numbering
GPIO.setmode(GPIO.BCM)
GPIO.setup(17, GPIO.IN)

bitSendTime = 5
byteSendTime = 50

# convert a binary array to a char
def arrayToLetter(binaryArray):
    output = 0
    for b in binaryArray:
        output = (output << 1) | b
    return chr(output)

while True:
        currentTime = time.time()
        milliseconds = ((currentTime * 1000) % byteSendTime)

        if milliseconds > 0.5 and  milliseconds < 1:
                binaryArray = []
                currentState = 0

                # start receiving
                for i in range(0, 8):
                        value = GPIO.input(17)
                        binaryArray.append(value)

#print milliseconds

                        # waiting
                        currentState = currentState + 1
                        while milliseconds < currentState * bitSendTime + 2:
                            currentTime = time.time()
                            milliseconds = ((currentTime * 1000) % byteSendTime)

#               print binaryArray
#               print arrayToLetter(binaryArray)
                sys.stdout.write(arrayToLetter(binaryArray))
                sys.stdout.flush()


