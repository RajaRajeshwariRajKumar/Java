
#!/usr/bin/python

import RPi.GPIO as GPIO
import time

bitSendTime = 5
byteSendTime = 50

slotSec = 0
sliceLength = 0
sliceCount = 0
sliceIndex = 0

def main(_text, _sliceLength, _sliceCount, _sliceIndex):
        # UseBCM numbering
        GPIO.setwarnings(False)
        GPIO.setmode(GPIO.BCM)
        GPIO.setup(23, GPIO.OUT)

        while True:
                sendText(_text, _sliceLength, _sliceCount, _sliceIndex)


# fill the binary array with a char
def letterToArray(letter):
    letterNumber = ord(letter)
    binaryArray = []
    for i in range(0, 8):
        binaryArray.append((letterNumber >> 7) & 0b1)
        letterNumber = (letterNumber << 1)
    return binaryArray

def textToAscii(inputText):
    for letter in inputText:
        binaryArray = letterToArray(letter)
        print binaryArray
        print arrayToLetter(binaryArray)


def sendText(inputText, sliceLength, sliceCount, sliceIndex):
        for letter in inputText:
                binaryArray= letterToArray(letter)
                print letter
                print binaryArray
                while True:

                    currentTime = time.time()
                    milliseconds = ((currentTime * 1000) % byteSendTime)
                    currentState = 0

                    if milliseconds < 1:
                        # start sending
                        for i in binaryArray:

                            currentTime = (time.time() % (sliceLength * sliceCount))
							
							# check if its the current slot
                            while (sliceLength * sliceIndex) + slotSec >= currentTime or currentTime >= (sliceLength * (sliceIndex + 1)) - slotSec:
                                currentTime = (time.time() % (sliceLength * sliceCount))

                            if i == 0 :
                                print "High"
                                GPIO.output(23, GPIO.HIGH)
                            else :
                                print "Low"
                                GPIO.output(23, GPIO.LOW)

                            print milliseconds

                            # waiting
                            currentState = currentState + 1
                            while milliseconds < currentState * bitSendTime:
                                currentTime = time.time()
                                milliseconds = ((currentTime * 1000) % byteSendTime)

                        # finished sending
                        break

