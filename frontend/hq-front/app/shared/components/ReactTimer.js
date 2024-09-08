import React, {useEffect} from 'react';
import { useTimer } from 'react-timer-hook';


const ReactTimer = ({ expiryTimestamp, leftText = undefined, rightText = undefined }) => {

    const {
        totalSeconds,
        seconds,
        minutes,
        hours,
        days,
        isRunning,
        start,
        pause,
        resume,
        restart,
    } = useTimer({ expiryTimestamp, onExpire: () => console.warn('onExpire called') });


    const formatDurationModified = (seconds) => {

        try {

            return new Date(seconds * 1000)
                .toISOString()
                .slice(11, 19);

        }catch (e){

            console.log(e);

            return "-";

        }
    }


    useEffect(()=>{
        if(expiryTimestamp !== undefined && expiryTimestamp != null) {
            restart(new Date().setSeconds(expiryTimestamp));
        }
    },[expiryTimestamp])

    const displayedDHMS = formatDurationModified(totalSeconds);

    return (
        <div style={{textAlign: 'center'}}>
            <div style={{fontSize: '14px'}}>
                {(!displayedDHMS || displayedDHMS === '00:00:00') ? <span></span> : <span>{leftText ? (<small>{leftText}</small>) : ""}{displayedDHMS}{rightText ? (<small>{rightText}</small>) : ""}</span>}
            </div>
            {/*      <p>{isRunning ? 'Running' : 'Not running'}</p>
      <button onClick={start}>Start</button>
      <button onClick={pause}>Pause</button>
      <button onClick={resume}>Resume</button>
      <button onClick={() => {
        // Restarts to 5 minutes timer
        const time = new Date();
        time.setSeconds(time.getSeconds() + 300);
        restart(time)
      }}>Restart</button>*/}
        </div>
    );
}

export default React.memo(ReactTimer);
