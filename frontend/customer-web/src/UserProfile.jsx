import PropTypes from 'prop-types';

const UserProfile = ({ name, age, gender, imageNumber }) => {
  gender = gender === 'MALE' ? 'men' : 'women';

  return (
    <div>
      <h1>{name}</h1>
      <p>{age}</p>
      <img
        src={`https://randomuser.me/api/portraits/${gender}/${imageNumber}.jpg`}
      />
    </div>
  );
};

UserProfile.propTypes = {
  name: PropTypes.string.isRequired,
  age: PropTypes.number.isRequired,
  gender: PropTypes.string.isRequired,
  imageNumber: PropTypes.number.isRequired,
};

export default UserProfile;
