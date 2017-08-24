from bs4 import BeautifulSoup
from selenium import webdriver


class Analyzer:
    """Class that analyzes links"""
    mainLink = 'http://mha1.nic.in/ipr/ips_ersheet_new/home.aspx'

    def __init__(self, year, cadre):
        self.year = year
        self.cadre = cadre
        self.links = []
        self.values = {}
        self.officers = []
        self.driver = webdriver.Chrome(executable_path='Driver/chromedriver')
        self.driver.get(self.mainLink)
        element = self.driver.find_element_by_id('form1')
        self.all_options = element.find_elements_by_tag_name("option")
        for option in self.all_options:
            self.values[option.get_attribute("value")] = option

    def analyze(self):
        for year in self.year:
            for cadre in self.cadre:

                self.values[str(year)].click()
                self.values[str(cadre)].click()
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
        self.driver.quit()

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
        educationInfo = list(self.findElement('table', 'id', 'gvEducation').find_all('td'))
        education = []
        for i in range(int(len(educationInfo) / 10)):
            tempEduRow = EducationRow(educationInfo[i * 10 + 1].text, educationInfo[i * 10 + 2].text,
                                      educationInfo[i * 10 + 3].text, educationInfo[i * 10 + 4].text,
                                      educationInfo[i * 10 + 5].text, educationInfo[i * 10 + 6].text,
                                      educationInfo[i * 10 + 7].text, educationInfo[i * 10 + 8].text,
                                      educationInfo[i * 10 + 9].text)
            education.append(tempEduRow)
        officer.setEducation(education)
        postings = []
        postingInfo = list(self.findElement('table', 'id', 'gvPostingExperience').find_all('td'))
        for i in range(int(len(postingInfo) / 10)):
            tempPostRow = PostingRow(postingInfo[i * 10 + 1].text, postingInfo[i * 10 + 2].text,
                                     postingInfo[i * 10 + 3].text, postingInfo[i * 10 + 4].text,
                                     postingInfo[i * 10 + 5].text, postingInfo[i * 10 + 6].text,
                                     postingInfo[i * 10 + 7].text, postingInfo[i * 10 + 8].text,
                                     postingInfo[i * 10 + 9].text)
            postings.append(tempPostRow)
        officer.setPosting(postings)
        training = []
        domesticTraining = list(self.findElement('table', 'id', 'gvDomesticTraining').find_all('td'))
        for i in range(int(len(domesticTraining) / 6)):
            tempTrainingRow = TrainingRow(domesticTraining[i * 6 + 1].text, domesticTraining[i * 6 + 2].text,
                                          domesticTraining[i * 6 + 3].text, domesticTraining[i * 6 + 4].text,
                                          domesticTraining[i * 6 + 5].text)
            training.append(tempTrainingRow)
        foreignTraining = list(self.findElement('table', 'id', 'gvForeignTraining').find_all('td'))
        for i in range(int(len(foreignTraining) / 6)):
            tempTrainingRow = TrainingRow(foreignTraining[i * 6 + 1].text, foreignTraining[i * 6 + 2].text,
                                          foreignTraining[i * 6 + 3].text, foreignTraining[i * 6 + 4].text,
                                          foreignTraining[i * 6 + 5].text)
            training.append(tempTrainingRow)
        officer.setTraining(training)
        self.officers.append(officer)

    def findElement(self, tag, atrKey, atrValue):
        return self.parser.find(str(tag), {str(atrKey): str(atrValue)})


class EducationRow:
    def __init__(self, qualification, discipline, spec1, spec2, year, division, institution, university, place):
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

    def __str__(self):
        s = ""
        s += ("Qualification:" + str(self.qualification) + " ")
        s += ("Discipline:" + str(self.discipline) + " ")
        s += ("spec1:" + str(self.spec1) + " ")
        s += ("spec2:" + str(self.spec2) + " ")
        s += ("Year:" + str(self.year) + " ")
        s += ("Division:" + str(self.division) + " ")
        s += ("Institution" + str(self.institution) + " ")
        s += ("University" + str(self.university) + " ")
        s += ("Place:" + str(self.place) + " ")
        return s


class PostingRow():
    def __init__(self, type, level, designation, ministry, place, experienceMajor, experienceMinor, startDate,
                 endDate, ):
        self.type = type
        self.level = level
        self.designation = designation
        self.ministry = ministry
        self.place = place
        self.experienceMajor = experienceMajor
        self.experienceMinor = experienceMinor
        self.startDate = startDate
        self.endDate = endDate

    def __str__(self):
        s = ""
        s += ("Type:" + str(self.type) + " ")
        s += ("Level:" + str(self.level) + " ")
        s += ("Designation:" + str(self.designation) + " ")
        s += ("Ministry:" + str(self.ministry) + " ")
        s += ("Place:" + str(self.place) + " ")
        s += ("Experience Major:" + str(self.experienceMajor) + " ")
        s += ("Experience Minor:" + str(self.experienceMinor) + " ")
        s += ("Start Date:" + str(self.startDate) + " ")
        s += ("End Date:" + str(self.endDate) + " ")
        return s


class TrainingRow():
    def __init__(self, courseName, courseTitle, place, startDate, endDate):
        self.courseName = courseName
        self.courseTitle = courseTitle
        self.place = place
        self.startDate = startDate
        self.endDate = endDate

    def __str__(self):
        s = ""
        s += ("Course Name:" + str(self.courseName) + " ")
        s += ("Course Title:" + str(self.courseTitle) + " ")
        s += ("Place:" + str(self.place) + " ")
        s += ("Start Date:" + str(self.startDate) + " ")
        s += ("End Date:" + str(self.endDate) + " ")
        return s


class Officer():
    def __init__(self, name, dob, cadre, batch, idNumber, dateOfAppointment, sourceOfRecuritment, ):
        if name[0:4] == 'Shri':
            self.gender = 'Shri'
            self.name = name[5:len(name)]
        elif name[0:4] == 'Smt.':
            self.gender = "Smt."
            self.name = name[5:len(name)]
        else:
            self.gender = ''
            self.name = name
            raise ValueError("ERROR: Gender cannot be identified")
        self.dob = dob
        self.cadre = cadre
        self.batch = batch
        self.idNumber = idNumber
        self.dateOfAppointment = dateOfAppointment
        self.sourceOfRecruitment = sourceOfRecuritment
        self.education = []
        self.postings = []
        self.training = []

    def setEducation(self, educationList):
        self.education = educationList

    def setPosting(self, postingList):
        self.postings = postingList

    def setTraining(self, trainingList):
        self.training = trainingList

    def __str__(self):
        s = ""
        s += "Gender:" + str(self.gender) + "\n"
        s += "Name:" + str(self.name) + "\n"
        s += "Date of birth:" + str(self.dob) + "\n"
        s += "Cadre:" + str(self.cadre) + "\n"
        s += "Batch:" + str(self.batch) + "\n"
        s += "Id Number:" + str(self.idNumber) + "\n"
        s += "Appointment Date:" + str(self.dateOfAppointment) + "\n"
        s += "Recruitment Source:" + str(self.sourceOfRecruitment) + "\n"
        s += "Education" + "\n"
        for educationRow in self.education:
            s += str(educationRow) + "\n"
        s += "Training" + "\n"
        for trainingRow in self.training:
            s += str(trainingRow) + "\n"
        s += "Postings" + "\n"
        for postingsRow in self.postings:
            s += str(postingsRow) + "\n"
        return s


years = [1988]
cadres = ['HY']
"""wb = openpyxl.Workbook()
wb.save("IPS DATA.xlsx")"""
a = Analyzer(years, cadres)
a.analyze()
for officer in a.officers:
    print(officer)
