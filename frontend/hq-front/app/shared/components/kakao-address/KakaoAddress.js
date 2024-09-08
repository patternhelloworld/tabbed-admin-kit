import React, { useHistory } from 'react'
import { useDaumPostcodePopup } from 'react-daum-postcode';
import {Button} from "../../../components";

const KakaoAddress = ({ setAddress = undefined }) => {

  const open = useDaumPostcodePopup();

  const handleComplete = (data) => {

    if (setAddress !== undefined && typeof setAddress === 'function') {
      const { zonecode, address, bname,  sido, sigungu} = data;

      setAddress({
        zipcode: zonecode,
        address: address,
        si: sido,
        gugun: sigungu, //시+군+구로 오지만 구+군으로 받음
        bname: bname
      });
    }
  }
  //클릭 시 발생할 이벤트
  const handleClick = () => {
    //주소검색이 완료되고, 결과 주소를 클릭 시 해당 함수 수행
    open({onComplete: handleComplete});
  }

  return <Button color={"dark"} outline size="sm" className="ml-auto" onClick={handleClick}>주소 검색</Button>

}

export default React.memo(KakaoAddress);
