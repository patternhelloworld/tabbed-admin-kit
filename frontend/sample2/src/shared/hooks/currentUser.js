import { get } from 'lodash';

import useApi from 'shared/hooks/api';

const useCurrentUser = ({ cachePolicy = 'cache-only' } = {}) => {

  const [{ data }] = useApi.get('/api/v1/users/me', {}, { cachePolicy });

  return {
    currentUser: data,
    currentUserId: get(data, 'id'),
  };
};

export default useCurrentUser;
