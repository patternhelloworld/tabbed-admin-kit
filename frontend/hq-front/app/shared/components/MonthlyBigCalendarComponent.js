import React, { useState, useRef } from "react";
import ReactDOM from "react-dom";

import FullCalendar from "@fullcalendar/react";
import dayGridPlugin from "@fullcalendar/daygrid";
import interactionPlugin from "@fullcalendar/interaction";
import googleCalendarPlugin from '@fullcalendar/google-calendar';

import koLocale from '@fullcalendar/core/locales/ko';

const apiKey = process.env.REACT_APP_CAL_API_KEY;
const MonthlyBigCalendarComponent = ({ onMonthChange, events, refreshAll, onEventClick, PK_NAME, loading, setLoading}) => {

    const calendarComponentRef = useRef(null);


    const handleEventClick = (info) => {
        info.jsEvent.preventDefault();
        if(info.event._def.extendedProps.hasOwnProperty(PK_NAME)){
            onEventClick(info.event._def.extendedProps);
        }
    };

    return (
        <div id="calendar-container">
            <FullCalendar
                schedulerLicenseKey="GPL-My-Project-Is-Open-Source"
                ref={calendarComponentRef}
                defaultView="dayGridMonth"
               // dateClick={handleDateClick}
                displayEventTime={false}
                header={{
                    left: "prev,next today",
                    center: "title",
                    right: "dayGridMonth,timeGridWeek,timeGridDay,listWeek",
                }}
                datesSet={(arg) => {
                    onMonthChange({
                        startDate : arg.start,
                        endDate : arg.end
                    })
                    setLoading(true)

                    if (events?.length === 0) {
                        setLoading(false); // No events, stop loading immediately
                    }
                }}
                plugins={[
                    dayGridPlugin,
                    interactionPlugin, googleCalendarPlugin
                ]}
                googleCalendarApiKey={apiKey}
                eventSources={[
                {
                    googleCalendarId: 'l9ijikc83v1ne5s61er6tava4iplm8id@import.calendar.google.com',
                    color: '#ef4f1e'
                }
                ]}
                events={events}
                eventDidMount={(info) => {
                    // 여기서 특정 조건에 따라 커서 모양을 설정합니다.
                    // 예시: 이벤트 제목이 특정 키워드를 포함하는 경우 커서 스타일을 변경
                    if (info.event._def.extendedProps.hasOwnProperty(PK_NAME)) {
                        info.el.style.cursor = 'pointer'; // 커서를 포인터로 설정
                    } else {
                        info.el.style.cursor = 'default'; // 커서를 none으로 설정
                    }

                    if(loading) {
                        setLoading(false)
                    }
                }}
              //  eventColor={'#447ef8'}
    /*            eventDisplay={'block'}
                eventTextColor={'#FFF'}
                eventColor={'#F2921D'}
                height={'660px'}*/
                eventClick={handleEventClick}
                selectable={false}
                //select={handleSelectedDates}
               // eventLimit={3}
                locale={koLocale}
            />
        </div>
    );
};

export default React.memo(MonthlyBigCalendarComponent);
