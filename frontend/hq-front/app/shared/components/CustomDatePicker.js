import React from 'react';
import DatePicker from 'react-datepicker';
import '../../assets/images/date-picker/react-datepicker.css'; // 경로 확인
import { getMonth, getYear } from 'date-fns';
import LeftArrow from '../../assets/images/date-picker/LeftArrow.svg';
import RightArrow from '../../assets/images/date-picker/RightArrow.svg';
import styles from '../../assets/images/date-picker/calendar.module.scss';

const YEARS = Array.from({ length: 2035 - 2010 + 1 }, (_, i) => 2035 - i);
const MONTHS = [
    'January',
    'February',
    'March',
    'April',
    'May',
    'June',
    'July',
    'August',
    'September',
    'October',
    'November',
    'December',
];

const CustomDatePicker = ({ selectedDate, setSelectedDate, disabled = false }) => {
    return (
        <div className={styles.datePickerWrapper}>
            <DatePicker
                disabled={disabled}
                dateFormat='yyyy.MM.dd'
                formatWeekDay={(nameOfDay) => nameOfDay.substring(0, 1)}
                showYearDropdown
                scrollableYearDropdown
                shouldCloseOnSelect
                yearDropdownItemNumber={100}
                minDate={new Date('2010-01-01')}
                maxDate={new Date('2099-12-31')}
                selected={selectedDate}
                calendarClassName={styles.calenderWrapper}
                dayClassName={(d) => (selectedDate && (d.getMonth() === selectedDate.getMonth())&& (d.getDate() === selectedDate.getDate()) ? styles.selectedDay : styles.unselectedDay)}
                onChange={(date) => setSelectedDate(date)}
                className={styles.datePicker}
                renderCustomHeader={({
                                         date,
                                         changeYear,
                                         decreaseMonth,
                                         increaseMonth,
                                         prevMonthButtonDisabled,
                                         nextMonthButtonDisabled,
                                     }) => (
                    <div className={styles.customHeaderContainer}>
                        <div>
                            <span className={styles.month}>{MONTHS[getMonth(date)]}</span>
                            <select
                                value={getYear(date)}
                                className={styles.year}
                                onChange={({ target: { value } }) => changeYear(+value)}
                            >
                                {YEARS.map((option) => (
                                    <option key={option} value={option}>
                                        {option}
                                    </option>
                                ))}
                            </select>
                        </div>
                        <div>
                            <button
                                type='button'
                                onClick={decreaseMonth}
                                className={styles.monthButton}
                                disabled={prevMonthButtonDisabled}
                            >
                               <img src={LeftArrow} className={styles.lArrow}/>
                            </button>
                            <button
                                type='button'
                                onClick={increaseMonth}
                                className={styles.monthButton}
                                disabled={nextMonthButtonDisabled}
                            >
                                <img src={RightArrow} className={styles.rArrow}/>
                            </button>
                        </div>
                    </div>
                )}
            />
        </div>
    );
};

export default CustomDatePicker;
