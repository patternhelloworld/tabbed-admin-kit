import React from 'react';
import { Flex } from '@mantine/core'; // Flex를 사용하는 라이브러리
import { FormFeedback } from 'reactstrap'; // FormFeedback 사용

const styles = {
    container: {
        flex: 1,
        minWidth: '300px',
    },
    label: {
        minWidth: '100px',
        textAlign: 'right',
        marginRight: '10px',
        fontWeight: "bold",
    },
};

const DoubleInputField = ({ label1, input1, error1, touched1, label2, input2, error2, touched2 }) => {
    return (
        <Flex gap="lg" wrap="wrap" className="mb-3">
            <Flex direction="row" align="center" style={styles.container}>
                <label style={styles.label} className="mt-2">
                    {label1}
                </label>
                {input1}
                {error1 && touched1 && (
                    <FormFeedback className="ml-3">{error1}</FormFeedback>
                )}
            </Flex>

            <Flex direction="row" align="center" style={styles.container}>
                <label style={styles.label} className="mt-2">
                    {label2}
                </label>
                {input2}
                {error2 && touched2 && (
                    <FormFeedback className="ml-3">{error2}</FormFeedback>
                )}
            </Flex>
        </Flex>
    );
};

export default React.memo(DoubleInputField);
