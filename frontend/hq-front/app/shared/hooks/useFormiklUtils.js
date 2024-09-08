import React, {useCallback, useEffect, useMemo, useState} from 'react';
import {isValidObject} from "../utils/utilities";

export const useFormikUtils = ({ formik, oneValidationSchema, initialTouched = true  }) => {

    const onKeyValueChangeByEvent = (e) => {
        formik.setFieldTouched(e.target.name);
        formik.handleChange(e);
    }

    const onKeyValueChangeByNameValue = ({name, value}) => {
        formik.setFieldTouched(name);
        formik.setFieldValue(name, value);
    }

    const onKeyValueChangeByEventMemoized = useCallback((e) => {
        formik.setFieldTouched(e.target.name);
        formik.handleChange(e);
    }, [formik]);

    const onKeyValueChangeByNameValueMemoized = useCallback(({ name, value}) => {
        formik.setFieldTouched(name);
        formik.setFieldValue(name, value);
    }, [formik]);

    const initializeFormikCommon = ({ one, modifiedOne, PK_NAME, customFormikSetOneFunc, customFormikSetModifiedOneFunc }) => {

        if(isValidObject(one)) {
            console.log("8-1 : one 이라는 객체에 값이 있습니다")
            console.log(one)
            console.log(modifiedOne)
            // modifiedOne : 수정한 one 인데, 이 화면에서 나가질 때만 저장.
            if(isValidObject(modifiedOne)){
                if(JSON.stringify(modifiedOne) !== JSON.stringify(one)
                    && modifiedOne[PK_NAME] === one[PK_NAME]){

                    // [주의] one[PK_NAME] === null 은 신규 입니다.
                    if(modifiedOne[PK_NAME] === undefined || one[PK_NAME] === undefined){
                        console.log("9-1" + one[PK_NAME] + " 또는 " + modifiedOne[PK_NAME] + "이 undeifined ")
                        if(customFormikSetOneFunc){
                            customFormikSetOneFunc(one)
                        }else{
                            formik.setValues({...formik.initialValues,...formik.values,...one})
                        }
                    }else {

                        // 탭 이동시 수정된 효과도 보존하기 위함
          /*              formik.setTouched({...Object.fromEntries(
                                Object.entries(oneValidationSchema.fields).map(([key, value]) => [key, true])
                            )
                        });*/
                        if(customFormikSetModifiedOneFunc){
                            customFormikSetModifiedOneFunc(modifiedOne)
                        }else{
                            formik.setValues({...formik.initialValues, ...formik.values, ...modifiedOne})
                        }

                        console.log("9-2 : 수정한 one 과 원래받은 one 이 다르고 && id 가 서로 같을때 ")
                    }

                }else{
                    if(customFormikSetOneFunc){
                        customFormikSetOneFunc(one)
                    }else{
                        formik.setValues({...formik.initialValues,...formik.values,...one})
                    }
                    console.log("10")
                }
            }else{
                if(customFormikSetOneFunc){
                    customFormikSetOneFunc(one)
                }else{
                    formik.setValues({...formik.initialValues,...formik.values,...one})
                }
                console.log("11")
            }

        }else{
            console.log("8-2 : one 이라는 객체에 값이 없어서 formik 에 One 이 반영되지 않았습니다.")
        }

    }

    useEffect(() => {
        if(initialTouched && oneValidationSchema && oneValidationSchema.hasOwnProperty("fields")) {
            formik.setTouched({
                ...Object.fromEntries(
                    Object.entries(oneValidationSchema.fields).map(([key, value]) => [key, true])
                )
            });
        }
    }, [formik.values])


    return {
        onKeyValueChangeByEvent,
        onKeyValueChangeByNameValue,
        onKeyValueChangeByEventMemoized,
        onKeyValueChangeByNameValueMemoized,
        initializeFormikCommon
    };
};
