import React from "react";

import AddBtn from 'assets/images/btns/simple-add.svg';
import UpdateBtn from 'assets/images/btns/simple-up.svg';
import DeleteBtn from 'assets/images/btns/simple-del.svg';

const simpleBtnImgs = {
    'add' : AddBtn,
    'update' : UpdateBtn,
    'delete' : DeleteBtn
};

const SimpleBtnImgs = ({ action, width, height }) => {
    const SvgIcon = simpleBtnImgs[action];
    return SvgIcon ? <img src={SvgIcon}
                          alt={`${action} Btn`}
                          style={{
                              width: `${width}`,
                              height: `${height}`,
                              opacity: 0.7,
                              transition: 'transform 0.2s ease, opacity 0.2s ease',
                          }}/> : null;
};

export default SimpleBtnImgs;