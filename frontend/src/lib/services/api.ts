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

const setAuthLocalStorageProps = (user: IUser, token: string) => {
    LocalStorage.set("user", user)
    LocalStorage.set("token", token)
}

export async function createUserAccount(user: INewUser) {
    try {
        const response = await axios.post(`${BASE_URL}/auth/signup`, user)
        const data = response.data as ApiResponse<{ user: IUser, token: string }>
        console.log(data)
        const { user: newUser, token } = data.data

        setAuthLocalStorageProps(newUser, token)

        return newUser
    } catch (e) {
        console.log(e)
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
        const { user: signedInUser, token } = data.data

        setAuthLocalStorageProps(signedInUser, token)

        return signedInUser
    } catch (e) {
        console.log(e)
        const err = (e as any).response.data as ApiErrorResponse
        throw err
    }
}

// ============================== GET USER
export async function getCurrentUser() {
    try {
        const token = LocalStorage.get("token") as string

        const response = await axios.get(`${BASE_URL}/users/me`, {
            headers: {
                Authorization: `Bearer ${token}`
            },
        })
        const post = response.data as ApiResponse<IUser>

        return post.data
    } catch (e) {
        console.log(e)
        const err = (e as any).response.data as ApiErrorResponse
        throw err
    }
}

// ============================== SIGN OUT
export async function signOutAccount() {
    LocalStorage.clear()
    return
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


export async function getSavedPosts() {
    const token = LocalStorage.get("token") as string
    try {
        const response = await axios.get(`${BASE_URL}/posts/saved`, {
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
export async function deletePost(postId?: number) {
    return withTryCatch(async () => {
        const token = LocalStorage.get("token") as string

        const response = await axios.delete(`${BASE_URL}/posts/delete/${postId}`, {
            headers: {
                Authorization: `Bearer ${token}`
            },
        })

        return response.data
    })
}

// ============================== LIKE / UNLIKE POST
export async function likePost(postId: number) {
    try {
        const token = LocalStorage.get("token") as string

        const response = await axios.post(`${BASE_URL}/posts/like/${postId}`, null, {
            headers: {
                Authorization: `Bearer ${token}`
            },
        })

        const post = response.data as ApiResponse<IPost>

        return post.data
    } catch (e) {
        const err = (e as any).response.data as ApiErrorResponse
        console.log(err || e)
        throw err
    }
}

// ============================== SAVE POST
export async function savePost(postId: number) {
    try {
        const token = LocalStorage.get("token") as string

        const response = await axios.post(`${BASE_URL}/posts/save/${postId}`, null, {
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

// ============================== DELETE SAVED POST
export async function deleteSavedPost(savedRecordId: number) {
    return withTryCatch(async () => {
        const token = LocalStorage.get("token") as string

        const response = await axios.delete(`${BASE_URL}/posts/save/${savedRecordId}`, {
            headers: {
                Authorization: `Bearer ${token}`
            },
        })

        return response.data
    })
}

export async function deleteLikedPost(savedRecordId: number) {
    return withTryCatch(async () => {
        const token = LocalStorage.get("token") as string

        const response = await axios.delete(`${BASE_URL}/posts/like/${savedRecordId}`, {
            headers: {
                Authorization: `Bearer ${token}`
            },
        })

        return response.data
    })
}

// ============================== GET USER'S POST
export async function getUserPosts(userId?: string) {
    return withTryCatch(async () => {
        const token = LocalStorage.get("token") as string

        const response = await axios.get(`${BASE_URL}/posts?userId=${userId}&page=1&perPage=100`, {
            headers: {
                Authorization: `Bearer ${token}`
            },
        })
        const post = response.data as ApiResponse<IPost>

        return post.data
    })
}

// ============================== GET POPULAR POSTS (BY HIGHEST LIKE COUNT)
export async function getRecentPosts() {
    return withTryCatch(async () => {
        const token = LocalStorage.get("token") as string

        const response = await axios.get(`${BASE_URL}/posts/recent`, {
            headers: {
                Authorization: `Bearer ${token}`
            },
        })
        const post = response.data as ApiResponse<IPost[]>

        return post.data
    })
}

// ============================================================
// USER
// ============================================================

// ============================== GET USERS
export async function getUsers(limit?: number) {
    return withTryCatch(async () => {
        const token = LocalStorage.get("token") as string

        const response = await axios.get(`${BASE_URL}/users?page=1&perPage=${limit || 20}`, {
            headers: {
                Authorization: `Bearer ${token}`
            },
        })
        const post = response.data as ApiResponse<IPost>

        return post.data
    })
}

// ============================== GET USER BY ID
export async function getUserById(userId: string) {
    return withTryCatch(async () => {
        const token = LocalStorage.get("token") as string

        const response = await axios.get(`${BASE_URL}/users/${userId}`, {
            headers: {
                Authorization: `Bearer ${token}`
            },
        })
        const post = response.data as ApiResponse<IUser>

        return post.data
    })
}

// ============================== UPDATE USER
export async function updateUser({ userId, file, name, bio }: IUpdateUser) {
    return withTryCatch(async () => {
        const token = LocalStorage.get("token") as string

        const form = new FormData();
        const json = JSON.stringify({ name, bio })
        const user = new Blob([json], {
            type: 'application/json'
        })
        if (file) {
            form.set("file", file[0])
        }
        form.set("user", user)

        const response = await axios.post(`${BASE_URL}/users/update/${userId}`, form, {
            headers: {
                Authorization: `Bearer ${token}`,
                "Content-Type": 'multipart/form-data'
            },
        })
        const data = response.data as ApiResponse<IPost>

        console.log(data)

        return data.data
    })
}

const withTryCatch = async (
    fn: any,
) => {
    try {
        return await fn()
    } catch (e) {
        console.log(e)
        const err = (e as any).response.data as ApiErrorResponse
        throw err
    }
}
