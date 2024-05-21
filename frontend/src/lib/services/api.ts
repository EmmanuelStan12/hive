import { IUpdatePost, INewPost, INewUser, IUpdateUser, IUser, IPost } from "@/types";
import axios from 'axios'

// ============================================================
// AUTH
// ============================================================

class LocalStorage {
    static get(property: string): any {
        const value = localStorage.getItem(property)
        if (!value) {
            return null
        }
        return JSON.parse(value)
    }

    static set(property: string, value: any) {
        localStorage.setItem(property, JSON.stringify(value))
    }

    static clear() {
        localStorage.clear()
    }
}

const BASE_URL = 'http://localhost:8080/api'

// ======================== INTERFACES

interface ApiResponse<T> {
    statusCode: number
    message: string
    data: T
}

interface ApiErrorResponse {
    statusCode: number
    timestamp: string
    message: string
    errors: any[]
}

const isTokenExpired = (token: string) => {
    const decodedToken = decodeURIComponent(atob(token.split('.')[1])); // Decode base64 encoded payload
    const tokenPayload = JSON.parse(decodedToken);
    const expirationTime = tokenPayload.exp;
    const currentTime = Math.floor(Date.now() / 1000); // Current time in seconds

    return expirationTime <= currentTime;
}

// ============================== SAVE USER TO DB
export async function saveUserToDB(user: {
    accountId: string;
    email: string;
    name: string;
    imageUrl: URL;
    username?: string;
}) {
    return null
}

const setAuthLocalStorageProps = (user: IUser, token: string) => {
    LocalStorage.set("user", user)
    LocalStorage.set("token", token)
}

export async function createUserAccount(user: INewUser) {
    try {
        const response = await axios.post(`${BASE_URL}/auth/signup`, user)
        const data = response.data as ApiResponse<{ user: IUser, token: string }>
        console.log(data)
        const { user, token } = data.data

        setAuthLocalStorageProps(user, token)

        return user
    } catch (e) {
        const err = (e as any).response.data as ApiErrorResponse
        throw err
    }
}

// ============================== SIGN IN
export async function signInAccount(signInUser: { email: string; password: string }) {
    try {
        const response = await axios.post(`${BASE_URL}/auth/login`, signInUser)
        const data = response.data as ApiResponse<{ user: IUser, token: string }>

        console.log(data)
        const { user, token } = data.data

        setAuthLocalStorageProps(user, token)

        return user
    } catch (e) {
        console.log(e)
        const err = (e as any).response.data as ApiErrorResponse
        throw err
    }
}

// ============================== GET ACCOUNT
export async function getAccount() {
    return null
}

// ============================== GET USER
export async function getCurrentUser() {
    const user = LocalStorage.get("user") as IUser
    if (!user) {
        return null
    }
    const token = LocalStorage.get("token") as string
    if (!isTokenExpired(token)) {
        return user
    }
    LocalStorage.clear()
    return null
}

// ============================== SIGN OUT
export async function signOutAccount() {
    return null
}

// ============================================================
// POSTS
// ============================================================

// ============================== CREATE POST
export async function createPost({ file, tags, caption, location }: INewPost) {
    tags = tags?.replace(/ /g, "");
    const token = LocalStorage.get("token") as string
    try {

        const form = new FormData();
        const json = JSON.stringify({ tags, caption, location })
        const post = new Blob([json], {
            type: 'application/json'
        })
        form.set("file", file[0])
        form.set("post", post)

        const response = await axios.post(`${BASE_URL}/posts/create`, form, {
            headers: {
                Authorization: `Bearer ${token}`,
                "Content-Type": 'multipart/form-data'
            },
        })
        const data = response.data as ApiResponse<IPost>

        console.log(data)

        return data.data
    } catch (e) {
        console.log(e)
        const err = (e as any).response.data as ApiErrorResponse
        throw err
    }

}

// ============================== GET POSTS
export async function searchPosts(searchTerm: string) {
    const token = LocalStorage.get("token") as string
    try {
        const response = await axios.get(`${BASE_URL}/posts?searchValue=${searchTerm}`, {
            headers: {
                Authorization: `Bearer ${token}`
            },
        })
        const data = response.data as ApiResponse<IPost[]>

        return data.data
    } catch (e) {
        console.log(e)
        const err = (e as any).response.data as ApiErrorResponse
        throw err
    }
}

export async function getInfinitePosts({ pageParam = 1 }: { pageParam: number }) {
    const token = LocalStorage.get("token") as string
    try {
        const response = await axios.get(`${BASE_URL}/posts?page=${pageParam}`, {
            headers: {
                Authorization: `Bearer ${token}`
            },
        })
        const data = response.data as ApiResponse<IPost[]>
        console.log(data)

        return data.data
    } catch (e) {
        console.log(e)
        const err = (e as any).response.data as ApiErrorResponse
        throw err
    }
}

// ============================== GET POST BY ID
export async function getPostById(postId?: string) {
    try {
        const token = LocalStorage.get("token") as string

        const response = await axios.get(`${BASE_URL}/posts/${postId}`, {
            headers: {
                Authorization: `Bearer ${token}`
            },
        })
        const post = response.data as ApiResponse<IPost>

        return post.data
    } catch (e) {
        console.log(e)
        const err = (e as any).response.data as ApiErrorResponse
        throw err
    }
}

// ============================== UPDATE POST
export async function updatePost({ tags, caption, file, postId, location }: IUpdatePost) {
    tags = tags?.replace(/ /g, "");
    const token = LocalStorage.get("token") as string
    try {

        const form = new FormData();
        const json = JSON.stringify({ tags, caption, location })
        const post = new Blob([json], {
            type: 'application/json'
        })
        if (file) {
            form.set("file", file[0])
        }
        form.set("post", post)

        const response = await axios.post(`${BASE_URL}/posts/update/${postId}`, form, {
            headers: {
                Authorization: `Bearer ${token}`,
                "Content-Type": 'multipart/form-data'
            },
        })
        const data = response.data as ApiResponse<IPost>

        console.log(data)

        return data.data
    } catch (e) {
        console.log(e)
        const err = (e as any).response.data as ApiErrorResponse
        throw err
    }
}

// ============================== DELETE POST
export async function deletePost(postId?: string, imageId?: string) {
    return null
}

// ============================== LIKE / UNLIKE POST
export async function likePost(postId: string, likesArray: string[]) {
    return null
}

// ============================== SAVE POST
export async function savePost(userId: string, postId: string) {
    return null
}

// ============================== DELETE SAVED POST
export async function deleteSavedPost(savedRecordId: string) {
    return null
}

// ============================== GET USER'S POST
export async function getUserPosts(userId?: string) {
    return null
}

// ============================== GET POPULAR POSTS (BY HIGHEST LIKE COUNT)
export async function getRecentPosts() {
    return null
}

// ============================================================
// USER
// ============================================================

// ============================== GET USERS
export async function getUsers(limit?: number) {
    return null
}

// ============================== GET USER BY ID
export async function getUserById(userId: string) {
    return null
}

// ============================== UPDATE USER
export async function updateUser(user: IUpdateUser) {
    return null
}
