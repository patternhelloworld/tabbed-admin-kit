import React from 'react';
import {useRecoilValue, useSetRecoilState } from 'recoil';

export const withRecoilValueHOC = (WrappedComponent, recoilValue) => {
    return function RecoilValueInjector(props) {
        const value = useRecoilValue(recoilValue);
        return <WrappedComponent {...props} recoilValue={value} />;
    };
};

export const withSetRecoilStateHOC = (WrappedComponent, recoilState) => {
    return function RecoilStateSetterInjector(props) {
        const setState = useSetRecoilState(recoilState);
        return <WrappedComponent {...props} setRecoilState={setState} />;
    };
};
