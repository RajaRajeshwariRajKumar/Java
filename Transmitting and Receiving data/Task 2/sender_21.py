import RPi.GPIO as GPIO
import time
import math

# set up GPIO
GPIO.setwarnings(False)
GPIO.setmode(GPIO.BCM)
GPIO.setup(23, GPIO.OUT)
GPIO.setup(24, GPIO.OUT)

while True:
   #both low around 500
   GPIO.output(23, GPIO.LOW)
   GPIO.output(24, GPIO.LOW)
   #1 high 1 low around 750
   time.sleep(0.5)
   GPIO.output(23,GPIO.HIGH)
   time.sleep(0.5)
   GPIO.output(23,GPIO.LOW)
   GPIO.output(24,GPIO.HIGH)
   time.sleep(0.5)
   #both high around 1000
   GPIO.output(23,GPIO.HIGH)
   time.sleep(0.5)
