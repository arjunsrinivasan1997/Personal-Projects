import os
from selenium import webdriver
from selenium.webdriver.chrome.options import Options
from bs4 import BeautifulSoup
from openpyxl import load_workbook
def findElement(tag, atrKey, atrValue):
    return parser.find(str(tag), {str(atrKey): str(atrValue)})
executable_path = "Driver/chromedriver"
proxyDriver = webdriver.Chrome(executable_path=executable_path)
proxyDriver.get("https://www.us-proxy.org")
el = proxyDriver.find_element_by_id('proxylisttable_length')
chrome_options = Options()
emails=["a@gmail.com","b@gmail.com","c@gmail.com","d@gmail.com","e@gmail.com","bucsrocknfl@gmail.com"]
driver = webdriver.Chrome(executable_path=executable_path)
driver.get("http://verify-email.org")
x = 0
for email in emails:
    form = driver.find_element_by_class_name("validate-email")
    form.send_keys(email)
    driver.find_element_by_class_name("submit").click()
    result = driver.find_element_by_class_name("resultsBox").find_element_by_class_name("alert")
    if "Result: Ok" in result.text:
        print("Email is valid")
    elif "Result: Bad" in result.text:
        print("EMAIL IS NOT VALID")
    elif "You can check only 5 email addresses per hour." in result.text:
        driver.quit()
        chrome_options = Options()

        driver = webdriver.Chrome(executable_path=executable_path, chrome_options=chrome_options)
        driver.get("http://verify-email.org")

