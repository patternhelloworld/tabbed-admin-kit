import React, {useCallback, useState, useEffect, useRef, Fragment} from 'react';
import RadioContext from "./RadioContext";

function RadioGroup({ label, children, ...rest }) {
    return (
        <Fragment>
            <RadioContext.Provider value={rest}>{children}</RadioContext.Provider>
        </Fragment>
    );
}

export default React.memo(RadioGroup);
