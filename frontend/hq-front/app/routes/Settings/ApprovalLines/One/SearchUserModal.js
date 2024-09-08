import React, { useCallback, useState, useEffect } from 'react'
import agent from "shared/api/agent";
import {renderError} from "shared/utils/CommonErrorHandler";

const TABLE_HEADERS = [
    { key: 'userIdx', label: '사용자ID' },
    { key: 'name', label: '이름' },
    { key: 'position', label: '직위' },
    { key: 'deptNm', label: '전시장' }
];

const fetchTableData = async () => {
    try {
        const re = await Promise.all([agent.User.fetch({
            skipPagination : true
        })]);

        if (!re[0].statusCode === 200) {
            throw new Error(`서버 응답 오류: ${re[0].statusCode}`);
        }

        const data = re[0].data;
        return data.content;

    } catch (error) {
        console.error('데이터 fetch 중 에러 발생:', error);
        return [];
    }
};

const SearchUserModal = ({ isOpen, onClose, onSelect }) => {
    const [filteredResults, setFilteredResults] = useState([]);
    const [allResults, setAllResults] = useState([]);
    const [searchQuery, setSearchQuery] = useState('');
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchData = async () => {
            try {
                const data = await fetchTableData();
                setFilteredResults(data);
                setAllResults(data);
            } catch (error) {
                setError('데이터를 가져오는 데 문제가 발생했습니다.');
            }
        };

        fetchData();
    }, []);

    useEffect(() => {
        const results = allResults.filter(result =>
            (result.name ? result.name.toLowerCase().includes(searchQuery.toLowerCase()) : '') ||
            (result.position ? result.position.toLowerCase().includes(searchQuery.toLowerCase()) : '') ||
            (result.deptNm ? result.deptNm.toLowerCase().includes(searchQuery.toLowerCase()) : '')
        );
        setFilteredResults(results);
    }, [searchQuery, allResults]);

    const handleResultClick = useCallback((result) => {
        if (onSelect) {
            onSelect(result);
            onClose();
        }
    }, [onSelect, onClose]);

    return isOpen ? (
        <div className="modal-overlay" onClick={onClose}>
            <div className="modal-content" onClick={(e) => e.stopPropagation()}>
                <span className="close" onClick={onClose}>&times;</span>
                <div className="search-user-container">
                    {error && <div className="error">{renderError(error)}</div>}
                    <div className="search-bar">
                        <input
                            type="text"
                            placeholder="검색어를 입력하세요..."
                            value={searchQuery}
                            onChange={(e) => setSearchQuery(e.target.value)}
                        />
                    </div>
                    <table className="user-table">
                        <thead>
                        <tr>
                            {TABLE_HEADERS.map(header => (
                                <th key={header.key}>{header.label}</th>
                            ))}
                        </tr>
                        </thead>
                        <tbody>
                        {filteredResults.map((result, index) => (
                            <tr key={index} onClick={() => handleResultClick(result)}>
                                <td>{result.userIdx}</td>
                                <td>{result.name}</td>
                                <td>{result.position}</td>
                                <td>{result.deptNm}</td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                </div>
            </div>
            <style jsx>{`
              .modal-overlay {
                position: fixed;
                top: 0;
                left: 0;
                width: 100%;
                height: 100%;
                background: rgba(0, 0, 0, 0.5);
                display: flex;
                justify-content: center;
                align-items: center;
                z-index: 1000;
              }

              .modal-content {
                background: #fff;
                border-radius: 8px;
                width: 80%;
                max-width: 850px;
                padding: 20px;
                position: relative;
                overflow: hidden;
                max-height: 80vh; /* 모달의 최대 높이 설정 */
              }

              .close {
                position: absolute;
                top: 10px;
                right: 10px;
                font-size: 24px;
                cursor: pointer;
              }

              .search-user-container {
                overflow-y: auto;
                max-height: calc(100% - 50px); /* 모달 내 콘텐츠 영역의 최대 높이 */
              }

              .search-bar {
                margin-top: 15px;
                margin-bottom: 10px;
              }

              .search-bar input {
                width: 100%;
                padding: 8px;
                font-size: 14px;
                border: 1px solid #ddd;
                border-radius: 4px;
              }

              .user-table {
                width: 100%;
                border-collapse: collapse;
                background-color: #f9f9f9;
              }

              .user-table th, .user-table td {
                border: 1px solid #ddd;
                padding: 8px;
                text-align: center;
              }

              .user-table th {
                background-color: #4CAF50;
                color: white;
              }

              .user-table tr:nth-child(even) {
                background-color: #f2f2f2;
              }

              .user-table tr:hover {
                background-color: #ddd;
                cursor: pointer;
              }

              .user-table td {
                font-size: 14px;
              }
            `}</style>
        </div>
    ) : null;
};

export default SearchUserModal;