from turtle import *
from math import *
from operator import *
from random import *

def change_color():
    colormode(255)
    a,b,c=randint(0,255),randint(0,255),randint(0,255)
    pencolor((a,b,c))
def make_shape(size):
    
    penup()
    bk(size/2)
    left(90)
    fd(size/2)
    def make_square(side_length):
        setheading(0)
        pendown()
        change_color()
        fd(side_length)
        rt(90)
        fd(side_length)
        rt(90)
        fd(side_length)
        rt(90)
        fd(side_length)
        rt(90)
    def make_diamond(length):
        change_color()
        penup()
        fd(length)
        pendown()
        rt(45)
        a=length* sqrt(2)
        fd(a)
        rt(90)
        fd(a)
        rt(90)
        fd(a)
        rt(90)
        fd(a)
        
    while size>10:
        make_square(size)
        make_diamond(size/2)
        penup()
        bk(size/2*sqrt(2)/2)
        size=size/2
    home()
def activator(size):
    

    make_shape(size)

    bk(size/2)
    left(90)
    fd(size/2)
    rt(90+45)
    pendown()
    change_color()
    fd(size*sqrt(2))
activator(400)
