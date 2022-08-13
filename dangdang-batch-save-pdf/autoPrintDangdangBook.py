import json
import os
from email.mime import base
from numpy import save
from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.common.desired_capabilities import DesiredCapabilities
from selenium.webdriver.support import expected_conditions as EC

baseUrl = "http://localhost:8000/"
baseSaveUrl = 'E:\\tmp'

#设置打印机的纸张大小、打印类型、保存路径等
edge_options = webdriver.EdgeOptions()
settings = {
    "recentDestinations": [{
        "id": "Save as PDF",
        "origin": "local",
        "account": ""
    }],
    "selectedDestinationId": "Save as PDF",
    "version": 2,
    "isHeaderFooterEnabled": False,

    # "customMargins": {},
    #"marginsType": 2,#边距（2是最小值、0是默认）
    # "scaling": 100,
    # "scalingType": 3,
    # "scalingTypePdf": 3,
    #"isLandscapeEnabled": True,  # 若不设置该参数，默认值为纵向
    "isCssBackgroundEnabled": True,
    "mediaSize": {
        "height_microns": 297000,
        "name": "ISO_A4",
        "width_microns": 210000,
        "custom_display_name": "A4"
    },
}

edge_options.add_argument('--enable-print-browser')
# chrome_options.add_argument('--headless') #headless模式下，浏览器窗口不可见，可提高效率
prefs = {
    'printing.print_preview_sticky_settings.appState': json.dumps(settings),
    'savefile.default_directory': 'E:\\tmp\\book'  # 此处填写你希望文件保存的路径,可填写your file path默认下载地址
}

edge_options.add_argument('--kiosk-printing')  # 静默打印，无需用户点击打印页面的确定按钮
edge_options.add_experimental_option('prefs', prefs)

edge_desired_capabilities = DesiredCapabilities.EDGE
edge_desired_capabilities["pageLoadStrategy"] = "NORMAL"

msedgedriver = 'D:\\program\\edgedriver_win64\\msedgedriver.exe'
driver = webdriver.Edge(executable_path=msedgedriver, options=edge_options)
driver.get(baseUrl)
driver.maximize_window()#浏览器最大化

wait = WebDriverWait(driver, 10)                                         # 创建一个WebDriver实例，并指定超时时间

parentList = wait.until(EC.presence_of_all_elements_located((By.TAG_NAME, 'li')))    # 调用 until() 方法，传入要等待的条件，until 表示一直等待，直到找到这个条件

# 是否打印标识
printTag = False
# 书籍列表
bookList = []
printTag = True
for i in range(len(parentList)):
    parent = parentList[i]
    # if parent.text == '程序员数学-用Python学透线性代数和微积分/':
    #     break
    # if parent.text == 'Spring源码深度解析(第2版)/':
    #     printTag = True
    # if printTag is False:
    #     continue
    saveUrl = 'E:\\tmp\\book\\' + parent.text[:-1]
    print(saveUrl)
    if os.path.exists(saveUrl):
        continue
    bookList.append(parent.text)
    
        
# 关闭浏览器
driver.close()

print(bookList)

for i in range(len(bookList)):
    parentName = bookList[i]
    urlP = baseUrl + parentName
    saveUrl = 'E:\\tmp\\book\\' + parentName[:-1]
    if os.path.exists(saveUrl) is False:
        os.makedirs(saveUrl)

    # 重新选择地址
    prefs['savefile.default_directory'] = saveUrl
    edge_options.add_experimental_option('prefs', prefs)
    driver = webdriver.Edge(executable_path=msedgedriver, options=edge_options)
    # 设置脚本执行超时
    driver.set_script_timeout(3000)
    driver.set_page_load_timeout(3000)
    
    driver.get(urlP)
    driver.maximize_window()#浏览器最大化
    wait = WebDriverWait(driver, 20)  
    kidList = wait.until(EC.presence_of_all_elements_located((By.TAG_NAME, 'li'))) 
    for j in range(len(kidList)):
        kid = kidList[j]
        kidName = kid.text
        title = kidName[:-1]
        urlK = urlP + kid.text
        print(urlK)
       
        # 遍历html页面，并且执行打印pdf
        if 'html' in urlK:
            print(saveUrl + '\\' + kid.text)
            # 如果文件已经存在，则不进行保存
            if os.path.exists(saveUrl + '\\' + kid.text[:-1] + '.pdf'):
                continue
            driver.get(urlK)
            wait = WebDriverWait(driver, 20) 
            driver.execute_script('document.title="' + title + '";window.print();')  # 利用js修改网页的title，该title最终就是PDF文件名，利用js的window.print可以快速调出浏览器打印窗口，避免使用热键ctrl+P
            # 重新进入上个页面，而且需要重新获取kidList
            driver.get(urlP)
            kidList = wait.until(EC.presence_of_all_elements_located((By.TAG_NAME, 'li'))) 
            # kidList = driver.find_elements((By.TAG_NAME, 'li'))
    # 关闭浏览器
    driver.close()
    