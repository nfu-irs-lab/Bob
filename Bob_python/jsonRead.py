import json

def getObjectsFormParticularGroup(group):
    objectsInGroup=[]
    f = open('jsons\\objects.json')
    array = json.load(f)
    for data in array:
        item_gruop=data["group"]
        item_name=data["name"]
        if item_gruop==group:
            objectsInGroup.append(item_name)
    f.close()
    return objectsInGroup

def getGroupList():
    groups=[]
    f = open('jsons\\groups.json')
    array = json.load(f)
    for data in array:
        groups.append(data['name'])
    f.close()
    return groups

print(getGroupList())
print(getObjectsFormParticularGroup("tableware"))