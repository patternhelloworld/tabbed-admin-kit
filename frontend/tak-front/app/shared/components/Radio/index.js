import React, {Fragment, useContext} from "react";
import RadioContext from "./RadioContext";
import {Input, Label} from "reactstrap";
import {onKeyValueChangeByEvent} from "../../utils/FormikUtils";

function Radio({ children, value, name, defaultChecked, disabled}) {
    const group = useContext(RadioContext);

    return (
        <Fragment>
            <Input type="radio"
                className={"form-check-input"}
                value={value}
                name={name}
                defaultChecked={defaultChecked}
                disabled={disabled || group.disabled}
                checked={group.value !== undefined ? value === group.value : undefined}
                onChange={group.onChange}
            />
            <Label check>
               {children}
            </Label>
        </Fragment>
    );
}

export default Radio;
