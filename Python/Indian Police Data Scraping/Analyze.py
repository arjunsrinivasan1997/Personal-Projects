from selenium import webdriver
from bs4 import BeautifulSoup
import csv
class Analyzer():
    mainLink = 'http://mha1.nic.in/ipr/ips_ersheet_new/home.aspx'

    def __init__(self, year, cadre):
        self.year = str(year)
        self.cadre = str(cadre)
        self.links = []
        self.values = {}
        self.officers=[]

    def analyze(self):
        self.driver = webdriver.Chrome(executable_path=r'Driver/chromedriver')
        self.driver.get("http://mha1.nic.in/ipr/ips_ersheet_new/home.aspx")
        element = self.driver.find_element_by_id('form1')
        self.all_options = element.find_elements_by_tag_name("option")
        for option in self.all_options:
            self.values[option.get_attribute("value")] = option
        self.values[self.year].click()
        self.values[self.cadre].click()
        self.driver.find_element_by_id("btnSubmit").click()
        self.links = self.driver.find_elements_by_xpath("//a[@href]")
        self.newLinks = self.driver.find_elements_by_xpath("//a[@href]")
        count = 0
        for link in self.links:
            if self.newLinks[count].text == "View ER SHEET":
                if count == 1:
                    self.links[count].click()
                else:
                    self.newLinks[count].click()
                self.parseLink()
                self.driver.back()
                self.newLinks = self.driver.find_elements_by_xpath("//a[@href]")
            count += 1

    def parseLink(self):
        self.parser = BeautifulSoup(self.driver.page_source, "html5lib")
        'Gathering Basic info'
        officer = Officer(self.findElement('span', 'id', 'lblName').text,
                          self.findElement('span', 'id', 'lblDOB').text,
                          self.findElement('span', 'id', 'lblCadre').text,
                          self.findElement('span', 'id', 'lblBatch').text,
                          self.findElement('span', 'id', 'lblIdentityNo').text,
                          self.findElement('span', 'id', 'lblDateOfAppointment').text,
                          self.findElement('span', 'id', 'lblSourceOfRecruitment').text)
        'Gathering Education Info Each row in the education table has 10 elements in it'
        educationTable = self.findElement('table', 'id', 'gvEducation')
        educationInfo = list(educationTable.find_all('td'))
        education = []
        for i in range(len(educationInfo) / 10):

            tempRow = EducationRow(educationInfo[i * 10 + 1].text, educationInfo[i * 10 + 2].text,
                                   educationInfo[i * 10 + 3].text, educationInfo[i * 10 + 4].text,
                                   educationInfo[i * 10 + 5].text, educationInfo[i * 10 + 6].text,
                                   educationInfo[i * 10 + 7].text, educationInfo[i * 10 + 8].text,
                                   educationInfo[i * 10 + 9].text)
            education.append(tempRow)
        officer.setEducation(education)
        self.officers.append(officer)
        postingTable = self.findElement('table','id','gvPostingExperience')
        postingInfo = list(postingTable.find_all('td'))


    def findElement(self, tag, atrKey, atrValue):
        return self.parser.find(str(tag), {str(atrKey): str(atrValue)})


class EducationRow():
    def __init__(self, qualification, discipline, spec1, spec2, year, division,
                 institution, university, place):
        self.qualification = qualification
        self.discipline = discipline
        if spec1 == "-":
            self.spec1 = ""
        else:
            self.spec1 = spec1
        if spec2 == "-":
            self.spec2 = ""
        else:
            self.spec2 = spec2
        self.year = year
        self.division = division
        self.institution = institution
        self.university = university
        self.place = place

    def prepCSV(self):
        info =[]
        x = 2


class Officer():
    def __init__(self, name, dob, cadre, batch, idNumber, dateOfAppointment, sourceOfRecuritment, ):

        if name[0:4]=='Shri':
            self.gender = 'Shri'
            self.name = name[5:len(name)]
        elif name[0:4]=='Shri' :
            self.gender = "Smt."
            self.name = name[5:len(name)]
        else :
            self.gender =''
            self.name=name
            print name + " Does not have a gender"
        self.dob = dob
        self.cadre = cadre
        self.batch = batch
        self.idNumber = idNumber
        self.dateOfAppointment = dateOfAppointment
        self.sourceOfRecruitment = sourceOfRecuritment
        self.education = []

    def setEducation(self, educationList):
        self.education = educationList

    def prepCSV(self):
        header =["Gender","Name","DOB","Cadre","Batch","IDNumber","Appointment Date",
                 "Source of Recruitment"]



test = Analyzer(1988, 'HY')
test.analyze()
