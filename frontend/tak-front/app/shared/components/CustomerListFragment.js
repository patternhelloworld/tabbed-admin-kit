import React from "react";

const styles = {
    inputsDiv: {
        padding: "20px",
        borderRadius: "8px",
        marginBottom : "20px",
        boxShadow: "0 2px 4px rgba(0, 0, 0, 0.1)"
    },
    commonDiv: {
        backgroundColor: "#e6f2ff",
        border: "1px solid #d6d6d6",
        padding: "13px",
        borderRadius: "10px",
        margin: "40px 0 10px 0",
        boxShadow: "0 4px 8px rgba(0, 0, 0, 0.1)"
    },
    commonFont: {
        color: "black",
        fontSize: "16px"
    },

    privacyDiv: {
        backgroundColor: "#fff3f3",
        border: "1px solid #d6d6d6",
        padding: "13px",
        borderRadius: "10px",
        margin: "30px 0 10px 0",
        boxShadow: "0 4px 8px rgba(0, 0, 0, 0.1)"
    },
    privacyFont: {
        color: "red",
        fontSize: "16px"
    }
}

export const InputsDiv = ({ children }) => {
    return (
        <div style={styles.inputsDiv}>
            {children}
        </div>
    );
}

export const InfoFrag =({title}) =>{
    return (
        <div style={styles.commonDiv}>
            <b style={styles.commonFont}>
                {title}
            </b>
        </div>
    );
}

export const CompanyFrag =() =>{
    return (
        <div style={styles.commonDiv}>
            <b style={styles.commonFont}>
                직장정보
            </b>
        </div>
    );
}

export const AgreesFrag =() =>{
    return (
        <div style={styles.commonDiv}>
            <b style={styles.commonFont}>
                수신 동의 여부 및 획득 경로
            </b>
        </div>
    );
}

export const InterestsFrag =() =>{
    return (
        <div style={styles.commonDiv}>
            <b style={styles.commonFont}>
                관심정보 및 기존 차량
            </b>
        </div>
    );
}

export const PrivacyFrag =() =>{
    return (
        <div style={styles.privacyDiv}>
            <b style={styles.privacyFont}>
                개인정보 활용동의서 파일 첨부 ( 반드시 개인정보 활용동의서 파일을 첨부 바랍니다 )
            </b>
        </div>
    );
}


