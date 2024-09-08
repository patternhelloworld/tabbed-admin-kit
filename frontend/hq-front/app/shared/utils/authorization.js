import classNames from "classnames";

export const availableElement = (originalClassNames = '', {accessTokenUserInfo = undefined, path = undefined,
    recoilKey = undefined,
    CRUD_COLUMN = undefined}) => {
    return classNames(originalClassNames, { 'd-none': !isAuthorized({accessTokenUserInfo, path, recoilKey, CRUD_COLUMN}) })
}

/*
*   path 난 recoilKey 중 하나는 명시해야 함
* */
export const isAuthorized = ({accessTokenUserInfo = undefined, path = undefined, recoilKey = undefined, CRUD_COLUMN = undefined}) => {
    if(accessTokenUserInfo === undefined){
        return false;
    }

    const permissions =  accessTokenUserInfo?.info?.permissions;
    if(permissions && Array.isArray(permissions)){
        if(path !== undefined) {
            if (CRUD_COLUMN !== undefined) {
                const permission = permissions.find(x => makeFullPath(x.mainMenuPath, x.subMenuPath) === path)
                if(permission){
                    if(permission[CRUD_COLUMN] === "Y") {
                        return true
                    }else{
                        return false
                    }
                }
            } else {
                return permissions.find(x => makeFullPath(x.mainMenuPath, x.subMenuPath) === path)
            }
        }else if(recoilKey !== undefined){
            if (CRUD_COLUMN !== undefined) {
                const permission = permissions.find(x => makeRecoilKey(x.mainMenuKey, x.subMenuKey) === recoilKey)
                if(permission){
                    if(permission[CRUD_COLUMN] === "Y") {
                        return true
                    }else{
                        return false
                    }
                }
            } else {
                return permissions.find(x => makeRecoilKey(x.mainMenuKey, x.subMenuKey) === recoilKey)

            }
        }else{
            throw new Error("path 또는 recoilKey 중 하나는 입력이 필수 입니다.")
        }

    }

}

export const makeFullPath = (mainMenuPath, subMenuPath) => {
    return "/" + mainMenuPath + "/" + subMenuPath;
}

export const makeRecoilKey = (mainMenuKey, subMenuKey) => {
    return mainMenuKey + subMenuKey;
}
export const CRUD_COLUMNS = {
    CREATE : "ynInt",
    READ : "ynLst",
    UPDATE : "ynMod",
    DELETE : "ynDel",
    EXCEL : "ynXls",
}