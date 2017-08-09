import smtplib
import sys
from datetime import date
from email.mime.multipart import MIMEMultipart
from email.mime.text import MIMEText
from openpyxl import load_workbook
from string import Template

numberOfEmailsToBeSent = 3
startRow = 2
if len(sys.argv) >= 2:
    try:
        numberOfEmailsToBeSent =int(sys.argv[1])
    except ValueError:
        raise ValueError("ERROR: Number of Emails to be sent must be an integer")
if len(sys.argv) == 3:
    try:
        startRow = int(sys.argv[2])
    except ValueError:
        raise ValueError("ERROR: Start Row must be an integer")
def read_template(filename):
    with open(filename, 'r', encoding='utf-8') as template_file:
        template_file_content = template_file.read()
    return Template(template_file_content)
'Setting Up Email Server'
senderAddress = "bucsrocknfl@gmail.com"
senderPassword = "Bucs#2002"
' Use smtp-relay.gmail.com if you have a GSuite Email and smtp.gmail.com otherwise '
s = smtplib.SMTP(host='smtp.gmail.com', port=587)
s.starttls()
s.login(senderAddress, senderPassword)
'Setting Up Excel Sheet'

wb = load_workbook('Outreach Track Sheet.xlsx')
ws = wb["Master List USA"]
currRow = startRow
template = read_template('template.txt')
for i in range(numberOfEmailsToBeSent):
    msg = MIMEMultipart()
    name = ws.cell(row=currRow,column=1).value
    email = ws.cell(row=currRow,column=2).value
    mediaType = ws.cell(row=currRow,column=4).value
    personalMessage = ws.cell(row=currRow,column=5).value
    if name == None:
        raise ValueError("ERROR: Name (row " + str(currRow) +",column 1) cannot be empty")
    if email == None:
        raise ValueError("ERROR: Email (row " + str(currRow) + ",column 2) cannot be empty")
    if mediaType == None:
        raise ValueError("ERROR: MediaType (row " + str(currRow) + ",column 4) cannot be empty")
    if personalMessage == None:
        raise ValueError("ERROR: Email (row " + str(currRow) + ",column 5) cannot be empty")
    body = template.substitute(PERSON_NAME=name,TYPE_OF_MEDIA=mediaType,Personal_Message=personalMessage)
    msg['From'] = senderAddress
    msg['To'] = email
    msg['Subject'] = "Cuddle Cub"
    msg.attach(MIMEText(body, 'plain'))

    s.send_message(msg)
    ws.cell(row=currRow,column=6).value="Angela"
    ws.cell(row=currRow,column=7).value = date.today()

    del msg
    currRow += 1






