import React, { useState, useRef } from "react";
import ReactDOM from "react-dom";

import FullCalendar from "@fullcalendar/react";
import dayGridPlugin from "@fullcalendar/daygrid";
import timeGridPlugin from "@fullcalendar/timegrid";
import interactionPlugin from "@fullcalendar/interaction";
import googleCalendarPlugin from '@fullcalendar/google-calendar';

import koLocale from '@fullcalendar/core/locales/ko';

const apiKey = process.env.REACT_APP_CAL_API_KEY;
const DailyBigCalendarComponent = ({ onMonthChange, events, refreshAll, onEventClick, PK_NAME, loading, setLoading}) => {

    const calendarComponentRef = useRef(null);


    const handleEventClick = (info) => {
        info.jsEvent.preventDefault();
        if (info.event._def.extendedProps.hasOwnProperty(PK_NAME)) {
            onEventClick(info.event._def.extendedProps);
        }
    };

    const handleDateClick = (info) => {
        // 날짜 클릭 시 일별 뷰로 전환
        const calendarApi = calendarComponentRef.current.getApi();
        calendarApi.changeView("timeGridDay", info.dateStr); // 일별 상세 보기로 전환
    };

    return (
        <div id="calendar-container">
            <FullCalendar
                schedulerLicenseKey="GPL-My-Project-Is-Open-Source"
                ref={calendarComponentRef}
                initialView="dayGridMonth"
                displayEventTime={false}
                headerToolbar={{
                    left: "prev,next today",
                    center: "title",
                    right: "dayGridMonth,timeGridWeek,timeGridDay,listWeek", // 주별/일별 뷰로 쉽게 전환 가능
                }}
                datesSet={(arg) => {
                    onMonthChange({
                        startDate: arg.start,
                        endDate: arg.end,
                    });
                    setLoading(true);


                    if (events?.length === 0) {
                        setLoading(false); // No events, stop loading immediately
                    }
                    setTimeout(function(){
                        setLoading(false);
                    }, 2000);
                }}
                plugins={[dayGridPlugin, timeGridPlugin, interactionPlugin, googleCalendarPlugin]} // 추가된 timeGridPlugin
   /*             googleCalendarApiKey={apiKey}
                eventSources={[
                    {
                        googleCalendarId:
                            "l9ijikc83v1ne5s61er6tava4iplm8id@import.calendar.google.com",
                        color: "#ef4f1e",
                    },
                ]}*/
                events={events}
                eventDidMount={(info) => {
                    if (info.event._def.extendedProps.hasOwnProperty(PK_NAME)) {
                        info.el.style.cursor = "pointer"; // 커서를 포인터로 설정
                    } else {
                        info.el.style.cursor = "default"; // 커서를 기본으로 설정
                    }

                    if (loading) {
                        setLoading(false);
                    }
                }}
                eventClick={handleEventClick}
                dateClick={handleDateClick} // 날짜 클릭 시 일별 뷰로 전환
                locale={koLocale}
            />
        </div>
    );
};

export default React.memo(DailyBigCalendarComponent);
