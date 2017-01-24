"Recursive Rainbow Representation"
"Initially written in scheme for the 2016 Scheme Art Contest"
from turtle import *
##############################################################
#helper functions#
def set_position(x,y):
    penup()
    setposition(x,y)
    pendown()
def color_picker(count):
    if 0 <= count <= 19:
        color((0.2941176475,0,0.509803922)) #indigo
    elif 20 <= count <= 39:
        color("blue")
    elif 40 <= count <= 59:
        color("green")
    elif 60 <= count <= 79:
        color("yellow")
    elif 80 <= count <= 99:
        color("orange")
    else:
        color("red")        
def make_circle(count):
    color_picker(count)
    fd(size)
    rt(121)
    if count == 118:
        prep_for_new_circle()
    else:
        count += 1
        make_circle(count)
def prep_for_new_circle():
    penup()
    setheading(0)
    fd(size / 2)
    rt(90)
    fd(size * (99 / 100))
    lt(90)
    bk(size / 2)
    pendown()
##############################################################

bgcolor("black")
speed(0)
size=75
number_of_rows=round(1400 / size)
height_change= size * (187.5 / 200)
initial_height= 5 * height_change
number_of_circles_per_column=round(900/size)
set_position(-670, initial_height)
for b in range(1,number_of_rows+1):
    for a in range(number_of_circles_per_column):
        make_circle(0)
    set_position(xcor()+size*(187.5 / 200),initial_height)
    
