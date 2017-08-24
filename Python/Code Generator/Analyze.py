from bs4 import BeautifulSoup
from selenium import webdriver

driver = webdriver.Chrome(executable_path='Driver/chromedriver')
driver.get("https://www.wix.com")
driver.find_element_by_id("wm-signin-link").click()
driver.find_element_by_name("email").send_keys("arjun@cuddle-cub.com")
driver.find_element_by_name("password").send_keys("cuddlecub2017")

