import React from "react";

import SquareNumber1 from 'assets/images/square-number/square-number-1.svg';
import SquareNumber2 from 'assets/images/square-number/square-number-2.svg';
import SquareNumber3 from 'assets/images/square-number/square-number-3.svg';
import SquareNumber4 from 'assets/images/square-number/square-number-4.svg';
import SquareNumber5 from 'assets/images/square-number/square-number-5.svg';
import SquareNumber6 from 'assets/images/square-number/square-number-6.svg';
import SquareNumber7 from 'assets/images/square-number/square-number-7.svg';
import SquareNumber8 from 'assets/images/square-number/square-number-8.svg';
import SquareNumber9 from 'assets/images/square-number/square-number-9.svg';

const squareNumbers = {
    1: SquareNumber1,
    2: SquareNumber2,
    3: SquareNumber3,
    4: SquareNumber4,
    5: SquareNumber5,
    6: SquareNumber6,
    7: SquareNumber7,
    8: SquareNumber8,
    9: SquareNumber9
};

const SquareNumberIcon = ({ number }) => {
    const SvgIcon = squareNumbers[number];
    return SvgIcon ? <img src={SvgIcon} alt={`Number ${number}`} width={24} height={24} /> : null;
};

export default SquareNumberIcon;