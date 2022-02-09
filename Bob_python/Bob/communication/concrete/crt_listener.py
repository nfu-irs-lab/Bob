from Bob.communication import PackageListener


class PrintedPackageListener(PackageListener):
    def onReceive(self, data: bytes):
        print(data.decode(), end='\n')
