from communication.framework.fw_listener import PackageListener


class PrintedPackageListener(PackageListener):
    def onReceive(self, data: bytes):
        print(data.decode(), end='\n')
