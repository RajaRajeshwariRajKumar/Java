#!/usr/bin/python

import RPi.GPIO as GPIO
import time

bitSendTime = 10
byteSendTime = 200

slotSec = 0
sliceLength = 0
sliceCount = 0
sliceIndex = 0

def main(_text, _sliceIndex):
        # UseBCM numbering
        GPIO.setwarnings(False)
        GPIO.setmode(GPIO.BCM)
        
        if _sliceIndex == 0:
          GPIO.setup(23, GPIO.OUT)
        else:         
          GPIO.setup(24, GPIO.OUT)

        while True:
                sendText(_text, _sliceIndex)


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


def sendText(inputText, senderIndex):
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
                         for j in range(0, 2):
                            # currentTime = (time.time() % (sliceLength * sliceCount))

                            # check if its the current slot
                            # while (sliceLength * sliceIndex) + slotSec >= currentTime or currentTime >= (sliceLength * (sliceIndex + 1)) - slotSec:
                            #    currentTime = (time.time() % (sliceLength * sliceCount))

                            if senderIndex == 0:
                             if i == 0:
                                if j == 0:
                                  GPIO.output(23, GPIO.HIGH)
                                if j == 1:
                                  GPIO.output(23, GPIO.HIGH)
                             if i == 1:
                                if j == 0:
                                  GPIO.output(23, GPIO.HIGH)
                                if j == 1:
                                  GPIO.output(23, GPIO.LOW)
                            
                            if senderIndex == 1:
                             if i == 0:
                                if j == 0:
                                  GPIO.output(24, GPIO.HIGH)
                                if j == 1:
                                  GPIO.output(24, GPIO.HIGH)
                             if i == 1:
                                if j == 0:
                                  GPIO.output(24, GPIO.LOW)
                                if j == 1:
                                  GPIO.output(24, GPIO.HIGH)                         
                             
                            print i
                            print j

                            # waiting
                            currentState = currentState + 1
                            while milliseconds < currentState * bitSendTime:
                                currentTime = time.time()
                                milliseconds = ((currentTime * 1000) % byteSendTime)

                        # finished sending
                        break

