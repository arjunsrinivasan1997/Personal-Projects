import requests
from lxml import html

values = {"ddlBatch":"1964","ddlCadre": "AP","tbName":"", "btnSubmit":"Submit"}
r = requests.post('http://mha1.nic.in/ipr/ips_ersheet_new/home.aspx',values)
print(r.text)

