import React from 'react';
import { Flex } from '@mantine/core';
import { FormFeedback } from 'reactstrap';

const styles = {
    container: {
        flex: 1,
        minWidth: '200px',
    },
    label: {
        minWidth: '100px',
        textAlign: 'right',
        marginRight: '10px',
        fontWeight: "bold",
    },
};

const TripleInputField = ({
                              label1, input1, error1, touched1,
                              label2, input2, error2, touched2,
                              label3, input3, error3, touched3
                          }) => {
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

            <Flex direction="row" align="center" style={styles.container}>
                <label style={styles.label} className="mt-2">
                    {label3}
                </label>
                {input3}
                {error3 && touched3 && (
                    <FormFeedback className="ml-3">{error3}</FormFeedback>
                )}
            </Flex>
        </Flex>
    );
};

export default React.memo(TripleInputField);
